package net.bramp.ffmpeg.info;

import junit.framework.TestCase;
import nl.jqno.equalsverifier.EqualsVerifier;

public class FormatTest extends TestCase {
  public void testFormatEqualsMethod() {
    EqualsVerifier.forClass(Format.class).verify();
  }
}
