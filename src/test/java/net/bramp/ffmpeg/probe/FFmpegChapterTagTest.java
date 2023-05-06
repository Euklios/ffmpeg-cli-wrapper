package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FFmpegChapterTagTest {
  @Test
  public void testFFmpegChapterTagEqualsMethod() {
    EqualsVerifier.simple().forClass(FFmpegChapterTag.class).verify();
  }
}
