package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class FFmpegChapterTest {
    @Test
    public void testFFmpegChapterEqualsMethod() {
        EqualsVerifier.simple().forClass(FFmpegChapter.class).verify();
    }
}
