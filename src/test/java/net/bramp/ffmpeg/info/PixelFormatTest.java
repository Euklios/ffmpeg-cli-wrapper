package net.bramp.ffmpeg.info;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class PixelFormatTest {
  @Test
  public void testPixelFormatEqualsMethod() {
    EqualsVerifier.forClass(PixelFormat.class).verify();
  }
}
