package net.bramp.ffmpeg.probe;

import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.List;

/** TODO Make this immutable */
@SuppressFBWarnings(
    value = {"UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"},
    justification = "POJO objects where the fields are populated by gson")
public final class FFmpegProbeResult {
  public FFmpegError error;
  public FFmpegFormat format;
  public List<FFmpegStream> streams;
  public List<FFmpegChapter> chapters;

  public FFmpegError getError() {
    return error;
  }

  public boolean hasError() {
    return error != null;
  }

  public FFmpegFormat getFormat() {
    return format;
  }

  public List<FFmpegStream> getStreams() {
    if (streams == null) return Collections.emptyList();
    return ImmutableList.copyOf(streams);
  }

  public List<FFmpegChapter> getChapters() {
    if (chapters == null) return Collections.emptyList();
    return ImmutableList.copyOf(chapters);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
