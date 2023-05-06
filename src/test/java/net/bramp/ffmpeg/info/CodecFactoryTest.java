package net.bramp.ffmpeg.info;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CodecFactoryTest {
  @Test
  public void createFailsIfNameIsNull() {
    assertThrows(NullPointerException.class, () -> CodecFactory.create(null, "test", "......"));
  }

  @Test
  public void createFailsIfLongNameIsNull() {
    assertThrows(NullPointerException.class, () -> CodecFactory.create("test", null, "......"));
  }

  @Test
  public void createFailsIfFlagsAreNull() {
    assertThrows(NullPointerException.class, () -> CodecFactory.create("test", "test", null));
  }

  @Test
  public void createFailsIfFlagsContainMoreThanSixCharacters() {
    assertThrows(
        IllegalArgumentException.class, () -> CodecFactory.create("test", "test", "1234567"));
  }

  @Test
  public void createFailsIfFlagsContainLessThanSixCharacters() {
    assertThrows(
        IllegalArgumentException.class, () -> CodecFactory.create("test", "test", "12345"));
  }

  @Test
  public void createFailsIfCodecTypeCharIsUnknown() {
    assertThrows(
        IllegalArgumentException.class, () -> CodecFactory.create("test", "test", "..X..."));
  }
}
