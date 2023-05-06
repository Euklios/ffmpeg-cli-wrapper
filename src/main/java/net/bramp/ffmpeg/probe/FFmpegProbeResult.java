package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record FFmpegProbeResult(
    @JsonProperty("error") FFmpegError error,
    @JsonProperty("format") FFmpegFormat format,
    @JsonProperty("streams") List<FFmpegStream> streams,
    @JsonProperty("chapters") List<FFmpegChapter> chapters) {
  public FFmpegError getError() {
    return error();
  }

  public boolean hasError() {
    return error() != null;
  }

  public FFmpegFormat getFormat() {
    return format();
  }

  public List<FFmpegStream> getStreams() {
    return streams();
  }

  public List<FFmpegChapter> getChapters() {
    return chapters();
  }
}
