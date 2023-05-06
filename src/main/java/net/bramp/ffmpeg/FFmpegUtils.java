package net.bramp.ffmpeg;

import static java.util.concurrent.TimeUnit.*;
import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotEmpty;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.base.CharMatcher;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.bramp.ffmpeg.jackson.FractionDeserializer;
import net.bramp.ffmpeg.jackson.FractionSerializer;
import net.bramp.ffmpeg.jackson.ImmutableListDeserializer;
import net.bramp.ffmpeg.jackson.ImmutableMapDeserializer;
import org.apache.commons.lang3.math.Fraction;

/** Helper class with commonly used methods */
public final class FFmpegUtils {
  static final ObjectMapper objectMapper = FFmpegUtils.setupObjectMapper();

  static final Pattern BITRATE_REGEX = Pattern.compile("(\\d+(?:\\.\\d+)?)kbits/s");
  static final Pattern TIME_REGEX = Pattern.compile("(\\d+):(\\d+):(\\d+(?:\\.\\d+)?)");
  static final CharMatcher ZERO = CharMatcher.is('0');

  FFmpegUtils() {
    throw new AssertionError("No instances for you!");
  }

  /**
   * Convert milliseconds to "hh:mm:ss.ms" String representation.
   *
   * @param milliseconds time duration in milliseconds
   * @return time duration in human-readable format
   * @deprecated please use #toTimecode() instead.
   */
  @Deprecated
  public static String millisecondsToString(long milliseconds) {
    return toTimecode(milliseconds, MILLISECONDS);
  }

  /**
   * Convert the duration to "hh:mm:ss" timecode representation, where ss (seconds) can be decimal.
   *
   * @param duration the duration.
   * @param units the unit the duration is in.
   * @return the timecode representation.
   */
  public static String toTimecode(long duration, TimeUnit units) {
    // FIXME Negative durations are also supported.
    // https://www.ffmpeg.org/ffmpeg-utils.html#Time-duration
    checkArgument(duration >= 0, "duration must be positive");

    long nanoseconds = units.toNanos(duration); // TODO This will clip at Long.MAX_VALUE
    long seconds = units.toSeconds(duration);
    long ns = nanoseconds - SECONDS.toNanos(seconds);

    long minutes = SECONDS.toMinutes(seconds);
    seconds -= MINUTES.toSeconds(minutes);

    long hours = MINUTES.toHours(minutes);
    minutes -= HOURS.toMinutes(hours);

    if (ns == 0) {
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    return ZERO.trimTrailingFrom(String.format("%02d:%02d:%02d.%09d", hours, minutes, seconds, ns));
  }

  /**
   * Returns the number of nanoseconds this timecode represents. The string is expected to be in the
   * format "hour:minute:second", where second can be a decimal number.
   *
   * @param time the timecode to parse.
   * @return the number of nanoseconds.
   */
  public static long fromTimecode(String time) {
    checkNotEmpty(time, "time must not be empty string");
    Matcher m = TIME_REGEX.matcher(time);
    if (!m.find()) {
      throw new IllegalArgumentException("invalid time '" + time + "'");
    }

    long hours = Long.parseLong(m.group(1));
    long mins = Long.parseLong(m.group(2));
    double secs = Double.parseDouble(m.group(3));

    return HOURS.toNanos(hours) + MINUTES.toNanos(mins) + (long) (SECONDS.toNanos(1) * secs);
  }

  /**
   * Converts a string representation of bitrate to a long of bits per second
   *
   * @param bitrate in the form of 12.3kbits/s
   * @return the bitrate in bits per second or -1 if bitrate is 'N/A'
   */
  public static long parseBitrate(String bitrate) {
    if ("N/A".equals(bitrate)) {
      return -1;
    }
    Matcher m = BITRATE_REGEX.matcher(bitrate);
    if (!m.find()) {
      throw new IllegalArgumentException("Invalid bitrate '" + bitrate + "'");
    }

    return (long) (Float.parseFloat(m.group(1)) * 1000);
  }

  static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  private static ObjectMapper setupObjectMapper() {
    final var mapper = new ObjectMapper();

    mapper.registerModule(setupObjectMapperModule());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

    return mapper;
  }

  private static Module setupObjectMapperModule() {
    SimpleModule simpleModule = new SimpleModule();

    simpleModule.addSerializer(Fraction.class, new FractionSerializer());
    simpleModule.addDeserializer(Fraction.class, new FractionDeserializer());

    simpleModule.setDeserializerModifier(
        new BeanDeserializerModifier() {
          @Override
          public JsonDeserializer<?> modifyCollectionDeserializer(
              DeserializationConfig config,
              CollectionType type,
              BeanDescription beanDesc,
              JsonDeserializer<?> deserializer) {
            return new ImmutableListDeserializer((CollectionDeserializer) deserializer);
          }

          @Override
          public JsonDeserializer<?> modifyMapDeserializer(
              DeserializationConfig config,
              MapType type,
              BeanDescription beanDesc,
              JsonDeserializer<?> deserializer) {
            return new ImmutableMapDeserializer((MapDeserializer) deserializer);
          }
        });

    return simpleModule;
  }
}
