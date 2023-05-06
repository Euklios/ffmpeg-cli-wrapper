package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class FFmpegStreamTest {
    @Test
    public void testFFmpegStreamEqualsMethod() {
        EqualsVerifier.simple().forClass(FFmpegStream.class).verify();
    }
}
