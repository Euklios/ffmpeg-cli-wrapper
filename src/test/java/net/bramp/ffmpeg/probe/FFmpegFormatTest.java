package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FFmpegFormatTest {
  @Test
  public void testFFmpegFormatEqualsMethod() {
    EqualsVerifier.simple().forClass(FFmpegFormat.class).verify();
  }
}
