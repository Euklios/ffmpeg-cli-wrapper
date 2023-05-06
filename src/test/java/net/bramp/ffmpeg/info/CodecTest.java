package net.bramp.ffmpeg.info;

import junit.framework.TestCase;
import nl.jqno.equalsverifier.EqualsVerifier;

public class CodecTest extends TestCase {
  public void testCodecEqualsMethod() {
    EqualsVerifier.forClass(Codec.class).verify();
  }
}
