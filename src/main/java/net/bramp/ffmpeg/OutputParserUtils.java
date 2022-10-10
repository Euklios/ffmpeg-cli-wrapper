package net.bramp.ffmpeg;

import com.google.common.collect.ImmutableList;
import net.bramp.ffmpeg.info.*;
import net.bramp.ffmpeg.io.ProcessUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputParserUtils {
  static final Pattern CODECS_REGEX = Pattern.compile("^ ([.D][.E][VASD][.I][.L][.S]) (\\S{2,})\\s+(.*)$");
  static final Pattern FORMATS_REGEX = Pattern.compile("^ ([ D][ E]) (\\S+)\\s+(.*)$");
  static final Pattern PIXEL_FORMATS_REGEX = Pattern.compile("^([.I][.O][.H][.P][.B]) (\\S{2,})\\s+(\\d+)\\s+(\\d+)$");
  static final Pattern HARDWARE_ACCELERATION_REGEX = Pattern.compile("^\\w+$");
  static final Pattern COLOR_REGEX = Pattern.compile("^(\\w+)\\s+#([0-9a-fA-F]{6})$");

  OutputParserUtils() {
    throw new AssertionError("No instances for you!");
  }

  private static <T> List<T> parseInformationOutput(ProcessFunction runFunc, String path, String flag, Function<String, Optional<T>> parsingFunction) throws IOException {
    List<T> results = new ArrayList<>();

    Process p = runFunc.run(ImmutableList.of(path, flag));
    try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
      String line;
      while ((line = r.readLine()) != null) {
        parsingFunction.apply(line).ifPresent(results::add);
      }

      ProcessUtils.throwOnError(path, p);
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

  public static List<PixelFormat> parsePixelFormats(ProcessFunction runFunc, String path) throws IOException {
    return parseInformationOutput(runFunc, path, "-pix_fmts", OutputParserUtils::parsePixelFormat);
  }

  private static Optional<PixelFormat> parsePixelFormat(String line) {
    Matcher m = PIXEL_FORMATS_REGEX.matcher(line);
    if (!m.matches()) return Optional.empty();

    return Optional.of(new PixelFormat(
        m.group(2),
        Integer.parseInt(m.group(3)),
        Integer.parseInt(m.group(4)),
        m.group(1)));
  }

  public static List<Protocol> parseProtocols(ProcessFunction runFunc, String path) throws IOException {
    final AtomicReference<Protocol.ProtocolDirection> currentParsingSection = new AtomicReference<>();
    return parseInformationOutput(runFunc, path, "-protocols", s -> parseProtocol(s, currentParsingSection));
  }

  private static Optional<Protocol> parseProtocol(String line, AtomicReference<Protocol.ProtocolDirection> currentParsingSection) {
    line = line.trim();

    if (line.equals("Input:")) {
      currentParsingSection.set(Protocol.ProtocolDirection.INPUT);
      return Optional.empty();
    } else if (line.equals("Output:")) {
      currentParsingSection.set(Protocol.ProtocolDirection.OUTPUT);
      return Optional.empty();
    } else if (line.endsWith(":")) {
      return Optional.empty();
    } else if (currentParsingSection.get() == null) {
      return Optional.empty();
    }

    return Optional.of(new Protocol(line, currentParsingSection.get()));
  }

  public static List<HardwareAccelerationModel> parseHardwareAccelerationModel(ProcessFunction runFunc, String path) throws IOException {
    return parseInformationOutput(runFunc, path, "-hwaccels", OutputParserUtils::parseHardwareAccelerationModel);
  }

  private static Optional<HardwareAccelerationModel> parseHardwareAccelerationModel(String line) {
    Matcher m = HARDWARE_ACCELERATION_REGEX.matcher(line);
    if (!m.matches()) return Optional.empty();

    return Optional.of(new HardwareAccelerationModel(line));
  }

  public static List<ColorDescriptor> parseColors(ProcessFunction runFunc, String path) throws IOException {
    return parseInformationOutput(runFunc, path, "-colors", OutputParserUtils::parseColor);
  }

  private static Optional<ColorDescriptor> parseColor(String line) {
    Matcher m = COLOR_REGEX.matcher(line);
    if (!m.matches()) return Optional.empty();

    return Optional.of(new ColorDescriptor(m.group(1), new Color(Integer.parseInt(m.group(2), 16))));
  }
}
