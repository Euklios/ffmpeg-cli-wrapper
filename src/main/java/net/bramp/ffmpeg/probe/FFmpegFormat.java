package net.bramp.ffmpeg.probe;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

@SuppressFBWarnings(
    value = {"UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
    justification = "POJO objects where the fields are populated by gson")
public final class FFmpegFormat {
  public String filename;
  public int nb_streams;
  public int nb_programs;

  public String format_name;
  public String format_long_name;
  public double start_time;

  /** Duration in seconds */
  // TODO Change this to java.time.Duration
  public double duration;

  /** File size in bytes */
  public long size;

  /** Bitrate */
  public long bit_rate;

  public int probe_score;

  public Map<String, String> tags;

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
