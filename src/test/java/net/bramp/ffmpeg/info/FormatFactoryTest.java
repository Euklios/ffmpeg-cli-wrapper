package net.bramp.ffmpeg.info;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FormatFactoryTest {

  @Test
  public void testCreateValidFormatFlags() {
    Format format1 = FormatFactory.create("mp4", "MPEG-4 Part 14", "DE");
    assertEquals("mp4", format1.name());
    assertEquals("MPEG-4 Part 14", format1.longName());
    assertTrue(format1.canDemux());
    assertTrue(format1.canMux());
  }

  @Test
  public void testCreateInvalidFormatFlags() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          FormatFactory.create("mp4", "MPEG-4 Part 14", "XXX");
        });
  }

  @Test
  public void testCreateNullName() {
    assertThrows(
        NullPointerException.class,
        () -> {
          FormatFactory.create(null, "MPEG-4 Part 14", "DE");
        });
  }

  @Test
  public void testCreateNullLongName() {
    assertThrows(
        NullPointerException.class,
        () -> {
          FormatFactory.create("mp4", null, "DE");
        });
  }

  @Test
  public void testCreateNullFlags() {
    assertThrows(
        NullPointerException.class,
        () -> {
          FormatFactory.create("mp4", "MPEG-4 Part 14", null);
        });
  }
}
