package net.bramp.ffmpeg.probe;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class FFmpegDispositionTest {
    @Test
    public void testFFmpegDispositionEqualsMethod() {
        EqualsVerifier.simple().forClass(FFmpegDisposition.class).verify();
    }
}
