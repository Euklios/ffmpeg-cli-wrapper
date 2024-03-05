package net.bramp.ffmpeg.probe;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Collections;
import java.util.Map;

@SuppressFBWarnings(
    value = {"UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD", "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
    justification = "POJO objects where the fields are populated by gson")
public class FFmpegFormat {
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

  public String getFilename() {
    return filename;
  }

  public int getNbStreams() {
    return nb_streams;
  }

  public int getNbPrograms() {
    return nb_programs;
  }

  public String getFormatName() {
    return format_name;
  }

  public String getFormatLongName() {
    return format_long_name;
  }

  public double getStartTime() {
    return start_time;
  }

  public double getDuration() {
    return duration;
  }

  public long getSize() {
    return size;
  }

  public long getBitRate() {
    return bit_rate;
  }

  public int getProbeScore() {
    return probe_score;
  }

  public Map<String, String> getTags() {
    return Collections.unmodifiableMap(tags);
  }
}
