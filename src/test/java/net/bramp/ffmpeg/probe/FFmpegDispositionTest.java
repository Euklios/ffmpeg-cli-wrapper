package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FFmpegDispositionTest {
  @Test
  public void testFFmpegDispositionEqualsMethod() {
    EqualsVerifier.simple().forClass(FFmpegDisposition.class).verify();
  }
}
