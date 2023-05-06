package net.bramp.ffmpeg.info;

public record PixelFormat(
    String name,
    int numberOfComponents,
    int bitsPerPixel,
    boolean canDecode,
    boolean canEncode,
    boolean hardwareAccelerated,
    boolean palettedFormat,
    boolean bitstreamFormat) {
  @Override
  public String toString() {
    return name;
  }

  /**
   * @deprecated Use {@link PixelFormat#name()} instead
   */
  @Deprecated(forRemoval = true)
  public String getName() {
    return name;
  }

  /**
   * @deprecated Use {@link PixelFormat#bitsPerPixel()} instead
   */
  @Deprecated(forRemoval = true)
  public int getBitsPerPixel() {
    return bitsPerPixel;
  }

  /**
   * @deprecated Use {@link PixelFormat#numberOfComponents()} instead
   */
  @Deprecated(forRemoval = true)
  public int getNumberOfComponents() {
    return numberOfComponents;
  }

  /**
   * @deprecated Use {@link PixelFormat#hardwareAccelerated()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isHardwareAccelerated() {
    return hardwareAccelerated;
  }

  /**
   * @deprecated Use {@link PixelFormat#palettedFormat()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isPalettedFormat() {
    return palettedFormat;
  }

  /**
   * @deprecated Use {@link PixelFormat#bitstreamFormat()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean isBitstreamFormat() {
    return bitstreamFormat;
  }
}
