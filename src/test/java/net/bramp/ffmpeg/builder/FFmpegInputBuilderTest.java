package net.bramp.ffmpeg.builder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class FFmpegInputBuilderTest {
  @Test
  public void testReadAtNativeFrameRatePosition() {
    assertCommand(List.of("-re", "-i", "input.mkv"), (builder) -> builder.readAtNativeFrameRate());
  }

  @Test
  public void testFormatPosition() {
    assertCommand(List.of("-f", "mp4", "-i", "input.mkv"), (builder) -> builder.setFormat("mp4"));
  }

  @Test
  public void testStartOffsetPosition() {
    assertCommand(
        List.of("-ss", "00:00:10", "-i", "input.mkv"),
        (builder) -> builder.setStartOffset(10, TimeUnit.SECONDS));
  }

  @Test
  public void testStartDuration() {
    assertCommand(
        List.of("-t", "00:00:10", "-i", "input.mkv"),
        (builder) -> builder.setDuration(10, TimeUnit.SECONDS));
  }

  protected void assertCommand(
      List<String> expected, Function<FFmpegInputBuilder, FFmpegInputBuilder> actual) {
    FFmpegBuilder parent = new FFmpegBuilder();
    assertEquals(expected, actual.apply(new FFmpegInputBuilder(parent, "input.mkv")).build(parent));
  }
}
