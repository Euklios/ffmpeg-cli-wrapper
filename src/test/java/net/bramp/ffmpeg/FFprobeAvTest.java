package net.bramp.ffmpeg;

import static net.bramp.ffmpeg.FFmpegTest.argThatHasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.google.gson.Gson;
import java.io.IOException;
import net.bramp.ffmpeg.builder.ProcessOptions;
import net.bramp.ffmpeg.fixtures.Samples;
import net.bramp.ffmpeg.lang.NewProcessAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Tests what happens when using avprobe */
@ExtendWith(MockitoExtension.class)
public class FFprobeAvTest {

  @Mock ProcessFunction runFunc;

  FFprobe ffprobe;

  static final Gson gson = FFmpegUtils.getGson();

  @BeforeEach
  public void before() throws IOException {
    when(runFunc.run(argThatHasItem("-version"), argThat(instanceOf(ProcessOptions.class))))
        .thenAnswer(new NewProcessAnswer("avprobe-version"));

    ffprobe = new FFprobe(runFunc);
  }

  @Test
  public void testVersion() throws Exception {
    assertEquals(
        "avprobe version 11.4, Copyright (c) 2007-2014 the Libav developers", ffprobe.version());
    assertEquals(
        "avprobe version 11.4, Copyright (c) 2007-2014 the Libav developers", ffprobe.version());
  }

  @Test
  public void testProbeVideo() throws IOException {
    assertThrows(
        IllegalArgumentException.class, () -> ffprobe.probe(Samples.big_buck_bunny_720p_1mb));
  }
}
