package net.bramp.ffmpeg.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.event.Level;

public class LoggerOutputStream extends OutputStream {

  final Logger logger;
  final Level level;

  final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

  public LoggerOutputStream(Logger logger, Level level) {
    this.logger = logger;
    this.level = level;
  }

  @Override
  public void write(int b) throws IOException {
    buffer.write(b);
    if (b == '\n') {
      String line = buffer.toString(StandardCharsets.UTF_8);
      switch (level) {
        case TRACE -> logger.trace(line);
        case DEBUG -> logger.debug(line);
        case INFO -> logger.info(line);
        case WARN -> logger.warn(line);
        case ERROR -> logger.error(line);
      }
      buffer.reset();
    }
  }
}
