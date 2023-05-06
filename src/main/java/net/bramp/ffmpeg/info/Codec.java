package net.bramp.ffmpeg.info;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Information about supported Codecs
 *
 * @author bramp
 */
public final class Codec {
  final String name;
  final String longName;

  /** Can I decode with this codec */
  final boolean canDecode;

  /** Can I encode with this codec */
  final boolean canEncode;

  /** What type of codec is this */
  final CodecType type;

  Codec(String name, String longName, boolean canDecode, boolean canEncode, CodecType type) {
    this.name = name;
    this.longName = longName;
    this.canDecode = canDecode;
    this.canEncode = canEncode;
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

  public CodecType getType() {
    return type;
  }
}
