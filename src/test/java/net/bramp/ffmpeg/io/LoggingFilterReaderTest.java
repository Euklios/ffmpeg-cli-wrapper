package net.bramp.ffmpeg.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class LoggingFilterReaderTest {

  private Reader reader;
  private Logger logger;
  private LoggingFilterReader loggingReader;

  @BeforeEach
  void setUp() {
    reader = new StringReader("Hello\nWorld!");
    logger = mock(Logger.class);
    loggingReader = new LoggingFilterReader(reader, logger);
  }

  @Test
  void testRead() throws IOException {
    StringBuilder sb = new StringBuilder();
    int c;
    while ((c = loggingReader.read()) != -1) {
      sb.append((char) c);
    }
    verify(logger).debug("Hello\n");
    verify(logger).debug("World!");
    assertEquals("Hello\nWorld!", sb.toString());
  }

  @Test
  void testReadCharArray() throws IOException {
    char[] buf = new char[5];
    int len;
    StringBuilder sb = new StringBuilder();
    while ((len = loggingReader.read(buf)) != -1) {
      sb.append(buf, 0, len);
    }
    verify(logger).debug("Hello\nWorl");
    verify(logger).debug("d!");
    assertEquals("Hello\nWorld!", sb.toString());
  }

  @Test
  void testReadCharArrayWithOffset() throws IOException {
    char[] buf = new char[20];
    int len;
    StringBuilder sb = new StringBuilder();
    while ((len = loggingReader.read(buf, 5, 10)) != -1) {
      sb.append(buf, 5, len);
    }
    verify(logger).debug("Hello\nWorl");
    verify(logger).debug("d!");
    assertEquals("Hello\nWorld!", sb.toString());
  }
}
