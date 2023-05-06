package net.bramp.ffmpeg.probe;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** Represents the AV_DISPOSITION_* fields */
@SuppressFBWarnings(
    value = {"UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"},
    justification = "POJO objects where the fields are populated by gson")
public final class FFmpegDisposition {
  public boolean _default;
  public boolean dub;
  public boolean original;
  public boolean comment;
  public boolean lyrics;
  public boolean karaoke;
  public boolean forced;
  public boolean hearing_impaired;
  public boolean visual_impaired;
  public boolean clean_effects;
  public boolean attached_pic;
  public boolean captions;
  public boolean descriptions;
  public boolean metadata;

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
