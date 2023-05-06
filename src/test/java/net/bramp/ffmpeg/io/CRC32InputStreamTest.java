package net.bramp.ffmpeg.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CRC32InputStreamTest {

  private CRC32InputStream crcStream;
  private ByteArrayInputStream byteStream;

  @BeforeEach
  public void setUp() {
    byte[] bytes = {1, 2, 3, 4, 5};
    byteStream = new ByteArrayInputStream(bytes);
    crcStream = new CRC32InputStream(byteStream);
  }

  // Tests for resetCrc() method

  @Test
  public void testResetCrc() throws IOException {
    crcStream.skip(2);
    crcStream.resetCrc();
    assertEquals(0x0L, crcStream.getValue());
  }

  // Tests for getValue() method

  @Test
  public void testGetValue() throws IOException {
    crcStream.skip(2);
    assertEquals(3066839698L, crcStream.getValue());
  }

  // Tests for read() method

  @Test
  public void testRead() throws IOException {
    byte[] buffer = new byte[3];
    int bytesRead = crcStream.read(buffer);
    assertEquals(3, bytesRead);
    assertEquals(1438416925, crcStream.getValue());
  }

  @Test
  public void testReadPartial() throws IOException {
    byte[] buffer = new byte[10];
    int bytesRead = crcStream.read(buffer, 2, 3);
    assertEquals(3, bytesRead);
    assertEquals(1438416925, crcStream.getValue());
    assertArrayEquals(new byte[] {0, 0, 1, 2, 3, 0, 0, 0, 0, 0}, buffer);
  }

  @Test
  public void testReadEmpty() throws IOException {
    byte[] buffer = new byte[0];
    int bytesRead = crcStream.read(buffer);
    assertEquals(0, bytesRead);
    assertEquals(0x0L, crcStream.getValue());
  }

  @Test
  public void testReadLargeBuffer() throws IOException {
    byte[] buffer = new byte[10];
    int bytesRead = crcStream.read(buffer);
    assertEquals(5, bytesRead);
    assertEquals(1191942644, crcStream.getValue());
    assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 0, 0, 0, 0, 0}, buffer);
  }

  @Test
  public void testReadTooLargeBuffer() {
    byte[] buffer = new byte[10];
    assertThrows(IndexOutOfBoundsException.class, () -> crcStream.read(buffer, 2, 10));
  }

  @Test
  public void testReadMultiple() throws IOException {
    byte[] buffer = new byte[2];
    int bytesRead1 = crcStream.read(buffer);
    assertEquals(2, bytesRead1);
    assertEquals(3066839698L, crcStream.getValue());
    int bytesRead2 = crcStream.read(buffer);
    assertEquals(2, bytesRead2);
    assertEquals(3057449933L, crcStream.getValue());
    int bytesRead3 = crcStream.read(buffer);
    assertEquals(1, bytesRead3);
    assertEquals(1191942644L, crcStream.getValue());
  }

  // Tests for skip() method

  @Test
  public void testSkip() throws IOException {
    long skipped = crcStream.skip(2);
    assertEquals(2, skipped);
    assertEquals(3066839698L, crcStream.getValue());
  }

  @Test
  public void testSkipLarge() throws IOException {
    long skipped = crcStream.skip(10);
    assertEquals(10, skipped);
    assertEquals(1191942644L, crcStream.getValue());
  }

  // Tests for mark() method

  @Test
  public void testMark() {
    assertThrows(UnsupportedOperationException.class, () -> crcStream.mark(10));
  }

  // Tests for reset() method

  @Test
  public void testReset() {
    assertThrows(UnsupportedOperationException.class, () -> crcStream.reset());
  }

  // Tests for markSupported() method

  @Test
  public void testMarkSupported() {
    assertFalse(crcStream.markSupported());
  }
}
