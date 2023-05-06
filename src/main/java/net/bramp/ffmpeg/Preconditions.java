package net.bramp.ffmpeg;

import java.net.URI;
import java.util.List;
import javax.annotation.Nullable;

public final class Preconditions {

  private static final List<String> rtps = List.of("rtsp", "rtp", "rtmp");
  private static final List<String> udpTcp = List.of("udp", "tcp");

  Preconditions() {
    throw new AssertionError("No instances for you!");
  }

  /**
   * Ensures the argument is not null, empty string, or just whitespace.
   *
   * @param arg The argument
   * @param errorMessage The exception message to use if the check fails
   * @return The passed in argument if it is not blank
   */
  public static String checkNotEmpty(String arg, @Nullable String errorMessage) {
    checkArgument(!(arg == null || arg.isBlank()), errorMessage);

    return arg;
  }

  /**
   * Ensures the argument is not null, empty string, or just whitespace.
   *
   * @param arg The argument
   * @return The passed in argument if it is not blank
   */
  public static String checkNotEmpty(String arg) {
    checkArgument(!(arg == null || arg.isBlank()));

    return arg;
  }

  /**
   * Ensures that an object passed as a parameter is not null.
   *
   * @param arg The argument
   * @return The passed in argument if it is not null
   */
  public static <T> T checkNotNull(@Nullable T arg) {
    if (arg == null) {
      throw new NullPointerException();
    }
    return arg;
  }

  /**
   * Ensures that an object passed as a parameter is not null.
   *
   * @param arg The argument
   * @param errorMessage The error message if the passed argument is null
   * @return The passed in argument if it is not null
   */
  public static <T> T checkNotNull(@Nullable T arg, String errorMessage) {
    if (arg == null) {
      throw new NullPointerException(errorMessage);
    }
    return arg;
  }

  /**
   * Checks if the URI is valid for streaming to.
   *
   * @param uri The URI to check
   * @return The passed in URI if it is valid
   * @throws IllegalArgumentException if the URI is not valid.
   */
  public static URI checkValidStream(URI uri) throws IllegalArgumentException {
    String scheme = checkNotNull(uri).getScheme();
    scheme = checkNotNull(scheme, "URI is missing a scheme").toLowerCase();

    if (rtps.contains(scheme)) {
      return uri;
    }

    if (udpTcp.contains(scheme)) {
      checkArgument(uri.getPort() != -1, "must set port when using udp or tcp scheme");

      return uri;
    }

    throw new IllegalArgumentException("not a valid output URL, must use rtp/tcp/udp scheme");
  }

  /**
   * Ensures the given condition is true
   *
   * @param expression a boolean to check
   */
  public static void checkArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Ensures the given condition is true
   *
   * @param expression a boolean to check
   * @param errorMessage The errorMessage for if expression is false
   */
  public static void checkArgument(boolean expression, String errorMessage) {
    if (!expression) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  /**
   * Ensures the given condition is true
   *
   * @param expression a boolean to check
   * @param errorMessage The errorMessage for if expression is false
   */
  public static void checkState(boolean expression, String errorMessage) {
    if (!expression) {
      throw new IllegalStateException(errorMessage);
    }
  }
}
