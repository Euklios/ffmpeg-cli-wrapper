package net.bramp.ffmpeg.info;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class PixelFormat {
  private final String name;
  private final int numberOfComponents;
  private final int bitsPerPixel;

  private final boolean canDecode;
  private final boolean canEncode;
  private final boolean hardwareAccelerated;
  private final boolean palettedFormat;
  private final boolean bitstreamFormat;

  PixelFormat(String name, int numberOfComponents, int bitsPerPixel, boolean canDecode, boolean canEncode, boolean hardwareAccelerated, boolean palettedFormat, boolean bitstreamFormat) {
    this.name = name;
    this.numberOfComponents = numberOfComponents;
    this.bitsPerPixel = bitsPerPixel;
    this.canDecode = canDecode;
    this.canEncode = canEncode;
    this.hardwareAccelerated = hardwareAccelerated;
    this.palettedFormat = palettedFormat;
    this.bitstreamFormat = bitstreamFormat;
  }

  @Override
  public String toString() {
    return name;
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

  public int getBitsPerPixel() {
    return bitsPerPixel;
  }

  public int getNumberOfComponents() {
    return numberOfComponents;
  }

  public boolean canEncode() {
    return canEncode;
  }

  public boolean canDecode() {
    return canDecode;
  }

  public boolean isHardwareAccelerated() {
    return hardwareAccelerated;
  }

  public boolean isPalettedFormat() {
    return palettedFormat;
  }

  public boolean isBitstreamFormat() {
    return bitstreamFormat;
  }
}
