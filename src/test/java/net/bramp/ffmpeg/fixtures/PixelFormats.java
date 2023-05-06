package net.bramp.ffmpeg.fixtures;

import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.info.PixelFormat;
import net.bramp.ffmpeg.info.PixelFormatFactory;

import java.util.List;

/**
 * Class that contains all pixel formats as defined in the unit tests This should not be used as a concise
 * list of available pixel formats, as every install of ffmpeg is different. Call ffmpeg.pixelFormats() to
 * discover.
 */
public final class PixelFormats
{

  private PixelFormats() {
    throw new AssertionError("No instances for you!");
  }

  public static final List<PixelFormat> PIXEL_FORMATS =
      new ImmutableListBuilder<PixelFormat>()
          .add(
              PixelFormatFactory.create("yuv420p", 3, 12, "IO..."),
              PixelFormatFactory.create("yuyv422", 3, 16, "IO..."),
              PixelFormatFactory.create("rgb24", 3, 24, "IO..."),
              PixelFormatFactory.create("bgr24", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv422p", 3, 16, "IO..."),
              PixelFormatFactory.create("yuv444p", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv410p", 3, 9, "IO..."),
              PixelFormatFactory.create("yuv411p", 3, 12, "IO..."),
              PixelFormatFactory.create("gray", 1, 8, "IO..."),
              PixelFormatFactory.create("monow", 1, 1, "IO..B"),
              PixelFormatFactory.create("monob", 1, 1, "IO..B"),
              PixelFormatFactory.create("pal8", 1, 8, "I..P."),
              PixelFormatFactory.create("yuvj420p", 3, 12, "IO..."),
              PixelFormatFactory.create("yuvj422p", 3, 16, "IO..."),
              PixelFormatFactory.create("yuvj444p", 3, 24, "IO..."),
              PixelFormatFactory.create("uyvy422", 3, 16, "IO..."),
              PixelFormatFactory.create("uyyvyy411", 3, 12, "....."),
              PixelFormatFactory.create("bgr8", 3, 8, "IO..."),
              PixelFormatFactory.create("bgr4", 3, 4, ".O..B"),
              PixelFormatFactory.create("bgr4_byte", 3, 4, "IO..."),
              PixelFormatFactory.create("rgb8", 3, 8, "IO..."),
              PixelFormatFactory.create("rgb4", 3, 4, ".O..B"),
              PixelFormatFactory.create("rgb4_byte", 3, 4, "IO..."),
              PixelFormatFactory.create("nv12", 3, 12, "IO..."),
              PixelFormatFactory.create("nv21", 3, 12, "IO..."),
              PixelFormatFactory.create("argb", 4, 32, "IO..."),
              PixelFormatFactory.create("rgba", 4, 32, "IO..."),
              PixelFormatFactory.create("abgr", 4, 32, "IO..."),
              PixelFormatFactory.create("bgra", 4, 32, "IO..."),
              PixelFormatFactory.create("gray16be", 1, 16, "IO..."),
              PixelFormatFactory.create("gray16le", 1, 16, "IO..."),
              PixelFormatFactory.create("yuv440p", 3, 16, "IO..."),
              PixelFormatFactory.create("yuvj440p", 3, 16, "IO..."),
              PixelFormatFactory.create("yuva420p", 4, 20, "IO..."),
              PixelFormatFactory.create("rgb48be", 3, 48, "IO..."),
              PixelFormatFactory.create("rgb48le", 3, 48, "IO..."),
              PixelFormatFactory.create("rgb565be", 3, 16, "IO..."),
              PixelFormatFactory.create("rgb565le", 3, 16, "IO..."),
              PixelFormatFactory.create("rgb555be", 3, 15, "IO..."),
              PixelFormatFactory.create("rgb555le", 3, 15, "IO..."),
              PixelFormatFactory.create("bgr565be", 3, 16, "IO..."),
              PixelFormatFactory.create("bgr565le", 3, 16, "IO..."),
              PixelFormatFactory.create("bgr555be", 3, 15, "IO..."),
              PixelFormatFactory.create("bgr555le", 3, 15, "IO..."),
              PixelFormatFactory.create("vaapi_moco", 0, 0, "..H.."),
              PixelFormatFactory.create("vaapi_idct", 0, 0, "..H.."),
              PixelFormatFactory.create("vaapi_vld", 0, 0, "..H.."),
              PixelFormatFactory.create("yuv420p16le", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv420p16be", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv422p16le", 3, 32, "IO..."),
              PixelFormatFactory.create("yuv422p16be", 3, 32, "IO..."),
              PixelFormatFactory.create("yuv444p16le", 3, 48, "IO..."),
              PixelFormatFactory.create("yuv444p16be", 3, 48, "IO..."),
              PixelFormatFactory.create("dxva2_vld", 0, 0, "..H.."),
              PixelFormatFactory.create("rgb444le", 3, 12, "IO..."),
              PixelFormatFactory.create("rgb444be", 3, 12, "IO..."),
              PixelFormatFactory.create("bgr444le", 3, 12, "IO..."),
              PixelFormatFactory.create("bgr444be", 3, 12, "IO..."),
              PixelFormatFactory.create("ya8", 2, 16, "IO..."),
              PixelFormatFactory.create("bgr48be", 3, 48, "IO..."),
              PixelFormatFactory.create("bgr48le", 3, 48, "IO..."),
              PixelFormatFactory.create("yuv420p9be", 3, 13, "IO..."),
              PixelFormatFactory.create("yuv420p9le", 3, 13, "IO..."),
              PixelFormatFactory.create("yuv420p10be", 3, 15, "IO..."),
              PixelFormatFactory.create("yuv420p10le", 3, 15, "IO..."),
              PixelFormatFactory.create("yuv422p10be", 3, 20, "IO..."),
              PixelFormatFactory.create("yuv422p10le", 3, 20, "IO..."),
              PixelFormatFactory.create("yuv444p9be", 3, 27, "IO..."),
              PixelFormatFactory.create("yuv444p9le", 3, 27, "IO..."),
              PixelFormatFactory.create("yuv444p10be", 3, 30, "IO..."),
              PixelFormatFactory.create("yuv444p10le", 3, 30, "IO..."),
              PixelFormatFactory.create("yuv422p9be", 3, 18, "IO..."),
              PixelFormatFactory.create("yuv422p9le", 3, 18, "IO..."),
              PixelFormatFactory.create("gbrp", 3, 24, "IO..."),
              PixelFormatFactory.create("gbrp9be", 3, 27, "IO..."),
              PixelFormatFactory.create("gbrp9le", 3, 27, "IO..."),
              PixelFormatFactory.create("gbrp10be", 3, 30, "IO..."),
              PixelFormatFactory.create("gbrp10le", 3, 30, "IO..."),
              PixelFormatFactory.create("gbrp16be", 3, 48, "IO..."),
              PixelFormatFactory.create("gbrp16le", 3, 48, "IO..."),
              PixelFormatFactory.create("yuva422p", 4, 24, "IO..."),
              PixelFormatFactory.create("yuva444p", 4, 32, "IO..."),
              PixelFormatFactory.create("yuva420p9be", 4, 22, "IO..."),
              PixelFormatFactory.create("yuva420p9le", 4, 22, "IO..."),
              PixelFormatFactory.create("yuva422p9be", 4, 27, "IO..."),
              PixelFormatFactory.create("yuva422p9le", 4, 27, "IO..."),
              PixelFormatFactory.create("yuva444p9be", 4, 36, "IO..."),
              PixelFormatFactory.create("yuva444p9le", 4, 36, "IO..."),
              PixelFormatFactory.create("yuva420p10be", 4, 25, "IO..."),
              PixelFormatFactory.create("yuva420p10le", 4, 25, "IO..."),
              PixelFormatFactory.create("yuva422p10be", 4, 30, "IO..."),
              PixelFormatFactory.create("yuva422p10le", 4, 30, "IO..."),
              PixelFormatFactory.create("yuva444p10be", 4, 40, "IO..."),
              PixelFormatFactory.create("yuva444p10le", 4, 40, "IO..."),
              PixelFormatFactory.create("yuva420p16be", 4, 40, "IO..."),
              PixelFormatFactory.create("yuva420p16le", 4, 40, "IO..."),
              PixelFormatFactory.create("yuva422p16be", 4, 48, "IO..."),
              PixelFormatFactory.create("yuva422p16le", 4, 48, "IO..."),
              PixelFormatFactory.create("yuva444p16be", 4, 64, "IO..."),
              PixelFormatFactory.create("yuva444p16le", 4, 64, "IO..."),
              PixelFormatFactory.create("vdpau", 0, 0, "..H.."),
              PixelFormatFactory.create("xyz12le", 3, 36, "IO..."),
              PixelFormatFactory.create("xyz12be", 3, 36, "IO..."),
              PixelFormatFactory.create("nv16", 3, 16, "....."),
              PixelFormatFactory.create("nv20le", 3, 20, "....."),
              PixelFormatFactory.create("nv20be", 3, 20, "....."),
              PixelFormatFactory.create("rgba64be", 4, 64, "IO..."),
              PixelFormatFactory.create("rgba64le", 4, 64, "IO..."),
              PixelFormatFactory.create("bgra64be", 4, 64, "IO..."),
              PixelFormatFactory.create("bgra64le", 4, 64, "IO..."),
              PixelFormatFactory.create("yvyu422", 3, 16, "IO..."),
              PixelFormatFactory.create("ya16be", 2, 32, "IO..."),
              PixelFormatFactory.create("ya16le", 2, 32, "IO..."),
              PixelFormatFactory.create("gbrap", 4, 32, "IO..."),
              PixelFormatFactory.create("gbrap16be", 4, 64, "IO..."),
              PixelFormatFactory.create("gbrap16le", 4, 64, "IO..."),
              PixelFormatFactory.create("qsv", 0, 0, "..H.."),
              PixelFormatFactory.create("mmal", 0, 0, "..H.."),
              PixelFormatFactory.create("d3d11va_vld", 0, 0, "..H.."),
              PixelFormatFactory.create("cuda", 0, 0, "..H.."),
              PixelFormatFactory.create("0rgb", 3, 24, "IO..."),
              PixelFormatFactory.create("rgb0", 3, 24, "IO..."),
              PixelFormatFactory.create("0bgr", 3, 24, "IO..."),
              PixelFormatFactory.create("bgr0", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv420p12be", 3, 18, "IO..."),
              PixelFormatFactory.create("yuv420p12le", 3, 18, "IO..."),
              PixelFormatFactory.create("yuv420p14be", 3, 21, "IO..."),
              PixelFormatFactory.create("yuv420p14le", 3, 21, "IO..."),
              PixelFormatFactory.create("yuv422p12be", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv422p12le", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv422p14be", 3, 28, "IO..."),
              PixelFormatFactory.create("yuv422p14le", 3, 28, "IO..."),
              PixelFormatFactory.create("yuv444p12be", 3, 36, "IO..."),
              PixelFormatFactory.create("yuv444p12le", 3, 36, "IO..."),
              PixelFormatFactory.create("yuv444p14be", 3, 42, "IO..."),
              PixelFormatFactory.create("yuv444p14le", 3, 42, "IO..."),
              PixelFormatFactory.create("gbrp12be", 3, 36, "IO..."),
              PixelFormatFactory.create("gbrp12le", 3, 36, "IO..."),
              PixelFormatFactory.create("gbrp14be", 3, 42, "IO..."),
              PixelFormatFactory.create("gbrp14le", 3, 42, "IO..."),
              PixelFormatFactory.create("yuvj411p", 3, 12, "IO..."),
              PixelFormatFactory.create("bayer_bggr8", 3, 8, "I...."),
              PixelFormatFactory.create("bayer_rggb8", 3, 8, "I...."),
              PixelFormatFactory.create("bayer_gbrg8", 3, 8, "I...."),
              PixelFormatFactory.create("bayer_grbg8", 3, 8, "I...."),
              PixelFormatFactory.create("bayer_bggr16le", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_bggr16be", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_rggb16le", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_rggb16be", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_gbrg16le", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_gbrg16be", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_grbg16le", 3, 16, "I...."),
              PixelFormatFactory.create("bayer_grbg16be", 3, 16, "I...."),
              PixelFormatFactory.create("xvmc", 0, 0, "..H.."),
              PixelFormatFactory.create("yuv440p10le", 3, 20, "IO..."),
              PixelFormatFactory.create("yuv440p10be", 3, 20, "IO..."),
              PixelFormatFactory.create("yuv440p12le", 3, 24, "IO..."),
              PixelFormatFactory.create("yuv440p12be", 3, 24, "IO..."),
              PixelFormatFactory.create("ayuv64le", 4, 64, "IO..."),
              PixelFormatFactory.create("ayuv64be", 4, 64, "....."),
              PixelFormatFactory.create("videotoolbox_vld", 0, 0, "..H.."),
              PixelFormatFactory.create("p010le", 3, 15, "IO..."),
              PixelFormatFactory.create("p010be", 3, 15, "IO..."),
              PixelFormatFactory.create("gbrap12be", 4, 48, "IO..."),
              PixelFormatFactory.create("gbrap12le", 4, 48, "IO..."),
              PixelFormatFactory.create("gbrap10be", 4, 40, "IO..."),
              PixelFormatFactory.create("gbrap10le", 4, 40, "IO..."),
              PixelFormatFactory.create("mediacodec", 0, 0, "..H.."),
              PixelFormatFactory.create("gray12be", 1, 12, "IO..."),
              PixelFormatFactory.create("gray12le", 1, 12, "IO..."),
              PixelFormatFactory.create("gray10be", 1, 10, "IO..."),
              PixelFormatFactory.create("gray10le", 1, 10, "IO..."),
              PixelFormatFactory.create("p016le", 3, 24, "IO..."),
              PixelFormatFactory.create("p016be", 3, 24, "IO..."),
              PixelFormatFactory.create("d3d11", 0, 0, "..H.."),
              PixelFormatFactory.create("gray9be", 1, 9, "IO..."),
              PixelFormatFactory.create("gray9le", 1, 9, "IO..."),
              PixelFormatFactory.create("gbrpf32be", 3, 96, "....."),
              PixelFormatFactory.create("gbrpf32le", 3, 96, "....."),
              PixelFormatFactory.create("gbrapf32be", 4, 128, "....."),
              PixelFormatFactory.create("gbrapf32le", 4, 128, "....."),
              PixelFormatFactory.create("drm_prime", 0, 0, "..H.."),
              PixelFormatFactory.create("opencl", 0, 0, "..H.."),
              PixelFormatFactory.create("gray14be", 1, 14, "IO..."),
              PixelFormatFactory.create("gray14le", 1, 14, "IO..."),
              PixelFormatFactory.create("grayf32be", 1, 32, "IO..."),
              PixelFormatFactory.create("grayf32le", 1, 32, "IO..."),
              PixelFormatFactory.create("yuva422p12be", 4, 36, "IO..."),
              PixelFormatFactory.create("yuva422p12le", 4, 36, "IO..."),
              PixelFormatFactory.create("yuva444p12be", 4, 48, "IO..."),
              PixelFormatFactory.create("yuva444p12le", 4, 48, "IO..."),
              PixelFormatFactory.create("nv24", 3, 24, "IO..."),
              PixelFormatFactory.create("nv42", 3, 24, "IO...")
          )
          .build();
}
