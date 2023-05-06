package net.bramp.ffmpeg.builder;

public enum Strict {
  VERY, // strictly conform to a older more strict version of the specifications or reference
  // software
  STRICT, // strictly conform to all the things in the specificiations no matter what consequences
  NORMAL, // normal
  UNOFFICIAL, // allow unofficial extensions
  EXPERIMENTAL;

  // ffmpeg command line requires these options in lower case
  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
