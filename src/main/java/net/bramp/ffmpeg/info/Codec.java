package net.bramp.ffmpeg.info;

/**
 * Information about supported Codecs
 *
 * @param canDecode Can I decode with this codec
 * @param canEncode Can I encode with this codec
 * @param type What type of codec is this
 *
 * @author bramp
 */
public record Codec(
        String name,
        String longName,
        boolean canDecode,
        boolean canEncode,
        CodecType type
) {

  @Override
  public String toString() {
    return name + " " + longName;
  }

  public String getName() {
    return name();
  }

  /**
   * @deprecated Use {@link Codec#name()} instead
   */
  @Deprecated(forRemoval = true)
  public String getLongName() {
    return longName();
  }

  /**
   * @deprecated Use {@link Codec#canDecode()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean getCanDecode() {
    return canDecode();
  }

  /**
   * @deprecated Use {@link Codec#canEncode()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean getCanEncode() {
    return canEncode();
  }

  /**
   * @deprecated Use {@link Codec#type()} instead
   */
  @Deprecated(forRemoval = true)
  public CodecType getType() {
    return type();
  }
}
