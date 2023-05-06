package net.bramp.ffmpeg.modelmapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.builder.Strict;
import net.bramp.ffmpeg.options.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;

public class MapperTest {
  @Test
  public void testMapping() {
    MainEncodingOptions main =
        new MainEncodingOptions("mp4", 0L, null, null, null, null, null, null, Strict.STRICT, 0);
    AudioEncodingOptions audio =
        new AudioEncodingOptions(false, null, 0, 0, null, 0, 0.0, null, null, null);
    VideoEncodingOptions video =
        new VideoEncodingOptions(
            true,
            null,
            null,
            320,
            240,
            1000,
            null,
            "scale='320:trunc(ow/a/2)*2'",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            true);
    SubtitleEncodingOptions subtitle = new SubtitleEncodingOptions(true, null, "vtt");

    EncodingOptions options = new EncodingOptions(main, audio, video, subtitle);

    FFmpegOutputBuilder mappedObj = new FFmpegOutputBuilder();

    Mapper.map(options, mappedObj);

    assertEquals(main.format(), mappedObj.getFormat());
    assertEquals(main.startOffset(), mappedObj.getStartOffset());
    assertEquals(main.duration(), mappedObj.getDuration());
  }

  @Test
  public void unmodifiedFFmpegOutputBuildersDoEqual() {
    assertTrue(
        EqualsBuilder.reflectionEquals(new FFmpegOutputBuilder(), new FFmpegOutputBuilder()));
  }

  @Test
  public void unmodifiedEncodingOptionsDoEqual() {
    assertTrue(
        EqualsBuilder.reflectionEquals(
            new FFmpegOutputBuilder().buildOptions(), new FFmpegOutputBuilder().buildOptions()));
  }
}
