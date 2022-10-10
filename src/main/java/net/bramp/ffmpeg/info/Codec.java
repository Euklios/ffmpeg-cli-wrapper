package net.bramp.ffmpeg.info;

import com.google.errorprone.annotations.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Information about supported Codecs
 *
 * @author bramp
 */
@Immutable
public class Codec {

  public enum Type {
    VIDEO,
    AUDIO,
    SUBTITLE,
    DATA
  }

  final String name;
  final String longName;

  /** Can I decode with this codec */
  final boolean canDecode;

  /** Can I encode with this codec */
  final boolean canEncode;

  /** Is intra frame-only codec */
  final boolean intraFrameOnly;

  /** Is Lossy compression */
  final boolean lossyCompression;

  /** Is Lossless compression */
  final boolean losslessCompression;

  /** What type of codec is this */
  final Type type;

  public Codec(String name, String longName, boolean canDecode, boolean canEncode, Type type, boolean intraFrameOnly, boolean lossyCompression, boolean losslessCompression) {
    this.name = name;
    this.longName = longName;
    this.canDecode = canDecode;
    this.canEncode = canEncode;
    this.intraFrameOnly = intraFrameOnly;
    this.lossyCompression = lossyCompression;
    this.losslessCompression = losslessCompression;
    this.type = type;
  }

  @Override
  public String toString() {
    return name + " " + longName;
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public String getName() {
    return name;
  }

  public String getLongName() {
    return longName;
  }

  public boolean getCanDecode() {
    return canDecode;
  }

  public boolean getCanEncode() {
    return canEncode;
  }

  public boolean getIntraFrameOnly() {
    return intraFrameOnly;
  }

  public boolean getLosslessCompression() {
    return losslessCompression;
  }

  public boolean getLossyCompression() {
    return lossyCompression;
  }

  public Type getType() {
    return type;
  }
}
