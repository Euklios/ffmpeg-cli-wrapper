package net.bramp.ffmpeg.builder;

/** Log level options: https://ffmpeg.org/ffmpeg.html#Generic-options */
public enum Verbosity {
  QUIET,
  PANIC,
  FATAL,
  ERROR,
  WARNING,
  INFO,
  VERBOSE,
  DEBUG;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
