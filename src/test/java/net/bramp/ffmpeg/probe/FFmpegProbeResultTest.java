package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FFmpegProbeResultTest {
  @Test
  public void testFFmpegProbeResultEqualsMethod() {
    EqualsVerifier.simple().forClass(FFmpegProbeResult.class).verify();
  }
}
