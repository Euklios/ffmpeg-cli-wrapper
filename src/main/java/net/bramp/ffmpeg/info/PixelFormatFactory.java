package net.bramp.ffmpeg.info;

import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotNull;

public class PixelFormatFactory {
  private PixelFormatFactory() {
    throw new AssertionError("No instances for you!");
  }

  public static PixelFormat create(
      String name, int numberOfComponents, int bitsPerPixel, String flags) {
    checkArgument(flags.length() == 5, "PixelFormat flags is invalid '%s'".formatted(flags));
    checkNotNull(name);

    boolean canDecode = flags.charAt(0) == 'I';
    boolean canEncode = flags.charAt(1) == 'O';
    boolean hardwareAccelerated = flags.charAt(2) == 'H';
    boolean palettedFormat = flags.charAt(3) == 'P';
    boolean bitstreamFormat = flags.charAt(4) == 'B';

    return new PixelFormat(
        name,
        numberOfComponents,
        bitsPerPixel,
        canDecode,
        canEncode,
        hardwareAccelerated,
        palettedFormat,
        bitstreamFormat);
  }
}
