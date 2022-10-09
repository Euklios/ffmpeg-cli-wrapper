package net.bramp.ffmpeg;

import com.google.common.collect.ImmutableList;
import net.bramp.ffmpeg.info.Codec;
import net.bramp.ffmpeg.info.Format;
import net.bramp.ffmpeg.io.ProcessUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputParserUtils {
  static final Pattern CODECS_REGEX = Pattern.compile("^ ([.D][.E][VASD][.I][.L][.S]) (\\S{2,})\\s+(.*)$");
  static final Pattern FORMATS_REGEX = Pattern.compile("^ ([ D][ E]) (\\S+)\\s+(.*)$");

  OutputParserUtils() {
    throw new AssertionError("No instances for you!");
  }

  private static <T> List<T> parseInformationOutput(ProcessFunction runFunc, String path, String flag, Function<String, Optional<T>> parsingFunction) throws IOException {
    List<T> results = new ArrayList<>();

    Process p = runFunc.run(ImmutableList.of(path, flag));
    try {
      BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
      String line;
      while ((line = r.readLine()) != null) {
        parsingFunction.apply(line).ifPresent(results::add);
      }

      throwOnError(path, p);
      return ImmutableList.copyOf(results);
    } finally {
      p.destroy();
    }
  }

  public static List<Codec> parseCodecs(ProcessFunction runFunc, String path) throws IOException {
    return parseInformationOutput(runFunc, path, "-codecs", OutputParserUtils::parseCodec);
  }

  private static Optional<Codec> parseCodec(String line) {
    Matcher m = CODECS_REGEX.matcher(line);
    if (!m.matches()) return Optional.empty();

    return Optional.of(new Codec(m.group(2), m.group(3), m.group(1)));
  }

  public static List<Format> parseFormats(ProcessFunction runFunc, String path) throws IOException {
    return parseInformationOutput(runFunc, path, "-formats", OutputParserUtils::parseFormat);
  }

  private static Optional<Format> parseFormat(String line) {
    Matcher m = FORMATS_REGEX.matcher(line);
    if (!m.matches()) return Optional.empty();

    return Optional.of(new Format(m.group(2), m.group(3), m.group(1)));
  }

  protected static void throwOnError(String path, Process p) throws IOException {
    try {
      // TODO In java 8 use waitFor(long timeout, TimeUnit unit)
      if (ProcessUtils.waitForWithTimeout(p, 1, TimeUnit.SECONDS) != 0) {
        // TODO Parse the error
        throw new IOException(path + " returned non-zero exit status. Check stdout.");
      }
    } catch (TimeoutException e) {
      throw new IOException("Timed out waiting for " + path + " to finish.");
    }
  }
}
