package net.bramp.ffmpeg.info;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PixelFormatFactoryTest {

  @Test
  void testCreate() {
    PixelFormat pf = PixelFormatFactory.create("RGB24", 3, 24, "IOHPB");
    assertEquals("RGB24", pf.name());
    assertEquals(3, pf.numberOfComponents());
    assertEquals(24, pf.bitsPerPixel());
    assertTrue(pf.canDecode());
    assertTrue(pf.canEncode());
    assertTrue(pf.hardwareAccelerated());
    assertTrue(pf.palettedFormat());
    assertTrue(pf.bitstreamFormat());
  }

  @Test
  void testCreateWithInvalidFlags() {
    assertThrows(
        IllegalArgumentException.class, () -> PixelFormatFactory.create("RGB24", 3, 24, "IOHP"));
    assertThrows(
        IllegalArgumentException.class, () -> PixelFormatFactory.create("RGB24", 3, 24, "IOHPBA"));
  }

  @Test
  void testCreateWithNullName() {
    assertThrows(NullPointerException.class, () -> PixelFormatFactory.create(null, 3, 24, "IOHPB"));
  }

  @Test
  void testCreateWithNullFlags() {
    assertThrows(NullPointerException.class, () -> PixelFormatFactory.create("RGB24", 3, 24, null));
  }
}
