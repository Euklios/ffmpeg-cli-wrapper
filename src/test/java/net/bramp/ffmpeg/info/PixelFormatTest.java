package net.bramp.ffmpeg.info;

import junit.framework.TestCase;
import nl.jqno.equalsverifier.EqualsVerifier;

public class PixelFormatTest extends TestCase {
    public void testPixelFormatEqualsMethod() {
        EqualsVerifier.forClass(PixelFormat.class).verify();
    }
}