package net.bramp.ffmpeg.modelmapper;

import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.AudioEncodingOptions;
import net.bramp.ffmpeg.options.EncodingOptions;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapperTest {

  @Test
  public void testMapping() {
    MainEncodingOptions main = new MainEncodingOptions("mp4", 0L, null);
    AudioEncodingOptions audio = new AudioEncodingOptions(false, null, 0, 0, null, 0, 0.0);
    VideoEncodingOptions video =
        new VideoEncodingOptions(
            true, null, null, 320, 240, 1000, null, "scale='320:trunc(ow/a/2)*2'", null);

    EncodingOptions options = new EncodingOptions(main, audio, video);

    FFmpegOutputBuilder mappedObj = new FFmpegOutputBuilder();

    Mapper.map(options, mappedObj);

    assertEquals(main.format(), mappedObj.format);
    assertEquals(main.startOffset(), mappedObj.startOffset);
    assertEquals(main.duration(), mappedObj.duration);
  }

  @Test
  public void unmodifiedFFmpegOutputBuildersDoEqual() {
    assertTrue(EqualsBuilder.reflectionEquals(new FFmpegOutputBuilder(), new FFmpegOutputBuilder()));
  }

  @Test
  public void unmodifiedEncodingOptionsDoEqual() {
    assertTrue(EqualsBuilder.reflectionEquals(new FFmpegOutputBuilder().buildOptions(), new FFmpegOutputBuilder().buildOptions()));
  }
}
