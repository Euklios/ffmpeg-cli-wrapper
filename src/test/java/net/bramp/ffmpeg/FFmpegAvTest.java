package net.bramp.ffmpeg;

import static net.bramp.ffmpeg.FFmpegTest.argThatHasItem;
import static net.bramp.ffmpeg.FFmpegTest.argThatIsInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import net.bramp.ffmpeg.builder.ProcessOptions;
import net.bramp.ffmpeg.lang.NewProcessAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests what happens when using avconv */
@ExtendWith(MockitoExtension.class)
public class FFmpegAvTest {

  @Mock ProcessFunction runFunc;

  FFmpeg ffmpeg;

  @BeforeEach
  public void before() throws IOException {
    when(runFunc.run(argThatHasItem("-version"), argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("avconv-version"));

    ffmpeg = new FFmpeg(runFunc);
  }

  @Test
  public void testVersion() throws Exception {
    assertEquals(
        "avconv version 11.4, Copyright (c) 2000-2014 the Libav developers", ffmpeg.version());
    assertEquals(
        "avconv version 11.4, Copyright (c) 2000-2014 the Libav developers", ffmpeg.version());
  }

  /** We don't support avconv, so all methods should throw an exception. */
  @Test
  public void testProbeVideo() {
    assertThrows(
        IllegalArgumentException.class,
        () -> ffmpeg.run(Collections.emptyList(), new ProcessOptions()));
  }

  @Test
  public void testCodecs() {
    assertThrows(IllegalArgumentException.class, () -> ffmpeg.codecs());
  }

  @Test
  public void testFormats() {
    assertThrows(IllegalArgumentException.class, () -> ffmpeg.formats());
  }
}
