package net.bramp.ffmpeg.info;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FormatTest {
  @Test
  public void testFormatEqualsMethod() {
    EqualsVerifier.forClass(Format.class).verify();
  }
}
