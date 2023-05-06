package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class FFmpegErrorTest {
    @Test
    public void testFFmpegErrorEqualsMethod() {
        EqualsVerifier.simple().forClass(FFmpegError.class).verify();
    }
}
