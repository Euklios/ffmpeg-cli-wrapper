package net.bramp.ffmpeg.info;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.io.ProcessUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parser collection for ffmpeg info commands
 *
 * @author euklios
 */
public final class InfoParser {
    static final Pattern CODECS_REGEX = Pattern.compile("^ ([.D][.E][VASD][.I][.L][.S]) (\\S{2,})\\s+(.*)$");
    static final Pattern FORMATS_REGEX = Pattern.compile("^ ([ D][ E]) (\\S+)\\s+(.*)$");
    static final Pattern PIXEL_FORMATS_REGEX = Pattern.compile("^([.I][.O][.H][.P][.B]) (\\S{2,})\\s+(\\d+)\\s+(\\d+)$");
    static final Pattern FILTERS_REGEX = Pattern.compile("^\\s*(?<timelinesupport>[T.])(?<slicethreading>[S.])(?<commandsupport>[C.])\\s(?<name>[A-Za-z0-9_]+)\\s+(?<inputpattern>[AVN|]+)->(?<outputpattern>[AVN|]+)\\s+(?<description>.*)$");


    private InfoParser() {
        throw new AssertionError("No instances for you!");
    }

    public static List<ChannelLayout> parseLayouts(BufferedReader r) throws IOException {
        Map<String, IndividualChannel> individualChannelLookup = new HashMap<>();
        List<ChannelLayout> channelLayouts = new ArrayList<>();

        String line;
        boolean parsingIndividualChannels = false;
        boolean parsingChannelLayouts = false;

        while ((line = r.readLine()) != null) {
            if (line.startsWith("NAME") || line.isEmpty()) {
                // Skip header and empty lines
                continue;
            } else if (line.equals("Individual channels:")) {
                parsingIndividualChannels = true;
                parsingChannelLayouts = false;
            } else if (line.equals("Standard channel layouts:")) {
                parsingIndividualChannels = false;
                parsingChannelLayouts = true;
            } else if (parsingIndividualChannels) {
                String[] s = line.split(" ", 2);
                IndividualChannel individualChannel = new IndividualChannel(s[0], s[1].trim());
                channelLayouts.add(individualChannel);
                individualChannelLookup.put(individualChannel.getName(), individualChannel);
            } else if (parsingChannelLayouts) {
                String[] s = line.split(" ", 2);
                List<IndividualChannel> decomposition = new ArrayList<>();
                for (String channelName :  Splitter.on('+').split(s[1].trim())) {
                    decomposition.add(individualChannelLookup.get(channelName));
                }

                channelLayouts.add(new StandardChannelLayout(s[0], Collections.unmodifiableList(decomposition)));
            }
        }

        return channelLayouts;
    }

    public static List<Codec> parseCodecs(FFmpeg ffmpeg) throws IOException {
        return parseInternal(ffmpeg, "-codecs", CODECS_REGEX, m -> new Codec(m.group(2), m.group(3), m.group(1)));
    }

    public static List<Format> parseFormats(FFmpeg ffmpeg) throws IOException {
        return parseInternal(ffmpeg, "-formats", FORMATS_REGEX, m -> new Format(m.group(2), m.group(3), m.group(1)));
    }

    public static List<PixelFormat> parsePixelFormats(FFmpeg ffmpeg) throws IOException {
        return parseInternal(ffmpeg, "-pix_fmts", PIXEL_FORMATS_REGEX, m -> {
            String flags = m.group(1);

            return new PixelFormat(m.group(2), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), flags);
        });
    }

    public static List<Filter> parseFilters(FFmpeg ffmpeg) throws IOException {
        return parseInternal(ffmpeg, "-filters", FILTERS_REGEX, m -> new Filter(
                m.group("timelinesupport").equals("T"),
                m.group("slicethreading").equals("S"),
                m.group("commandsupport").equals("C"),
                m.group("name"),
                new FilterPattern(m.group("inputpattern")),
                new FilterPattern(m.group("outputpattern")),
                m.group("description")
        ));

    }

    private static <T> List<T> parseInternal(FFmpeg ffmpeg, String option, Pattern pattern, Function<Matcher, T> mapper) throws IOException {
        Process p = ffmpeg.getRunFunction().run(ImmutableList.of(ffmpeg.getPath(), option));
        List<T> settings = new ArrayList<>();
        try(BufferedReader r = wrapInReader(p)) {
            String line;
            while((line = r.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (!m.matches()) continue;

                settings.add(mapper.apply(m));
            }

            throwOnError(ffmpeg, p);
            return ImmutableList.copyOf(settings);
        } finally {
            p.destroy();
        }
    }

    private static BufferedReader wrapInReader(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
    }

    private static void throwOnError(FFmpeg ffmpeg, Process p) throws IOException {
        try {
            // TODO In java 8 use waitFor(long timeout, TimeUnit unit)
            if (ProcessUtils.waitForWithTimeout(p, 1, TimeUnit.SECONDS) != 0) {
                // TODO Parse the error
                throw new IOException(ffmpeg.getPath() + " returned non-zero exit status. Check stdout.");
            }
        } catch (TimeoutException e) {
            throw new IOException("Timed out waiting for " + ffmpeg.getPath() + " to finish.");
        }
    }
}
