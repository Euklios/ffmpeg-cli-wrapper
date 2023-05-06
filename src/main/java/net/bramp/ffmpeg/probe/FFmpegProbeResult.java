package net.bramp.ffmpeg.probe;

import com.google.gson.annotations.JsonAdapter;
import net.bramp.ffmpeg.gson.ImmutableListAdapter;

import java.util.List;

public record FFmpegProbeResult(
        FFmpegError error,
        FFmpegFormat format,
        @JsonAdapter(value = ImmutableListAdapter.class, nullSafe = false)
        List<FFmpegStream> streams,
        @JsonAdapter(value = ImmutableListAdapter.class, nullSafe = false)
        List<FFmpegChapter> chapters
) {
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
