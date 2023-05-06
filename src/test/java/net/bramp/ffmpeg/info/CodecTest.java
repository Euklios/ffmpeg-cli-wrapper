package net.bramp.ffmpeg.info;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class CodecTest {
  @Test
  public void testCodecEqualsMethod() {
    EqualsVerifier.forClass(Codec.class).verify();
  }
}
