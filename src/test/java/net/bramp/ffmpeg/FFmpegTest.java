package net.bramp.ffmpeg;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.io.IOException;
import java.util.List;

import net.bramp.ffmpeg.fixtures.*;
import net.bramp.ffmpeg.lang.NewProcessAnswer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FFmpegTest {

  @Mock ProcessFunction runFunc;

  FFmpeg ffmpeg;

  @Before
  public void before() throws IOException {
    when(runFunc.run(argThatHasItem("-version")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-version"));
    when(runFunc.run(argThatHasItem("-formats")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-formats"));
    when(runFunc.run(argThatHasItem("-codecs"))).thenAnswer(new NewProcessAnswer("ffmpeg-codecs"));
    when(runFunc.run(argThatHasItem("-pix_fmts"))).thenAnswer(new NewProcessAnswer("ffmpeg-pix_fmts"));
    when(runFunc.run(argThatHasItem("-protocols"))).thenAnswer(new NewProcessAnswer("ffmpeg-protocols"));
    when(runFunc.run(argThatHasItem("-hwaccels"))).thenAnswer(new NewProcessAnswer("ffmpeg-hwaccels"));
    when(runFunc.run(argThatHasItem("-colors"))).thenAnswer(new NewProcessAnswer("ffmpeg-colors"));

    ffmpeg = new FFmpeg(runFunc);
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> argThatHasItem(T s) {
    return (List<T>) argThat(hasItem(s));
  }

  @Test
  public void testVersion() throws Exception {
    assertEquals("ffmpeg version 0.10.9-7:0.10.9-1~raring1", ffmpeg.version());
    assertEquals("ffmpeg version 0.10.9-7:0.10.9-1~raring1", ffmpeg.version());

    verify(runFunc, times(1)).run(argThatHasItem("-version"));
  }

  @Test
  public void testCodecs() throws IOException {
    // Run twice, the second should be cached
    assertEquals(Codecs.CODECS, ffmpeg.codecs());
    assertEquals(Codecs.CODECS, ffmpeg.codecs());

    verify(runFunc, times(1)).run(argThatHasItem("-codecs"));
  }

  @Test
  public void testFormats() throws IOException {
    // Run twice, the second should be cached
    assertEquals(Formats.FORMATS, ffmpeg.formats());
    assertEquals(Formats.FORMATS, ffmpeg.formats());

    verify(runFunc, times(1)).run(argThatHasItem("-formats"));
  }

  @Test
  public void testPixelFormat() throws IOException {
    // Run twice, the second should be cached
    assertEquals(PixelFormats.PIXEL_FORMATS, ffmpeg.pixelFormats());
    assertEquals(PixelFormats.PIXEL_FORMATS, ffmpeg.pixelFormats());

    verify(runFunc, times(1)).run(argThatHasItem("-pix_fmts"));
  }

  @Test
  public void testProtocols() throws IOException {
    // Run twice, the second should be cached
    assertEquals(Protocols.PROTOCOLS, ffmpeg.protocols());
    assertEquals(Protocols.PROTOCOLS, ffmpeg.protocols());

    verify(runFunc, times(1)).run(argThatHasItem("-protocols"));
  }

  @Test
  public void testHardwareAccelerationModels() throws IOException {
    // Run twice, the second should be cached
    assertEquals(HardwareAccelerationModels.HARDWARE_ACCELERATION_MODELS, ffmpeg.hardwareAccelerationModels());
    assertEquals(HardwareAccelerationModels.HARDWARE_ACCELERATION_MODELS, ffmpeg.hardwareAccelerationModels());

    verify(runFunc, times(1)).run(argThatHasItem("-hwaccels"));
  }

  @Test
  public void testColors() throws IOException {
    // Run twice, the second should be cached
    assertEquals(ColorDescriptors.COLORS, ffmpeg.colors());
    assertEquals(ColorDescriptors.COLORS, ffmpeg.colors());

    verify(runFunc, times(1)).run(argThatHasItem("-colors"));
  }
}
