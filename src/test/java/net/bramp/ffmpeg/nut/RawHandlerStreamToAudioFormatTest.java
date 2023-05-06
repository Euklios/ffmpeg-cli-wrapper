package net.bramp.ffmpeg.nut;

import static javax.sound.sampled.AudioFormat.Encoding.*;
import static net.bramp.ffmpeg.nut.StreamHeaderPacket.AUDIO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import org.apache.commons.lang3.math.Fraction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RawHandlerStreamToAudioFormatTest {

  public static List<Arguments> data() {
    return Arrays.asList(
        Arguments.of(
            new StreamHeaderPacket(AUDIO, "ALAW".getBytes(), Fraction.getFraction(48000, 1), 2),
            new AudioFormat(ALAW, 48000, 8, 2, 2, 48000, false)),
        Arguments.of(
            new StreamHeaderPacket(AUDIO, "ULAW".getBytes(), Fraction.getFraction(48000, 1), 3),
            new AudioFormat(ULAW, 48000, 8, 3, 3, 48000, false)),
        Arguments.of(
            new StreamHeaderPacket(
                AUDIO, "PSD\u0008".getBytes(), Fraction.getFraction(48000, 1), 4),
            new AudioFormat(PCM_SIGNED, 48000, 8, 4, 4, 48000, false)),
        Arguments.of(
            new StreamHeaderPacket(
                AUDIO, "\u0010DUP".getBytes(), Fraction.getFraction(48000, 1), 6),
            new AudioFormat(PCM_UNSIGNED, 48000, 16, 6, 12, 48000, true)),
        Arguments.of(
            new StreamHeaderPacket(
                AUDIO, "PFD\u0020".getBytes(), Fraction.getFraction(48000, 1), 8),
            new AudioFormat(PCM_FLOAT, 48000, 32, 8, 32, 48000, false)));
  }

  @ParameterizedTest
  @MethodSource("data")
  public void testStreamToAudioFormat(StreamHeaderPacket stream, AudioFormat expected) {
    AudioFormat format = RawHandler.streamToAudioFormat(stream);

    // Compare strings since AudioFormat does not have a good equalsCode(..) method.
    assertThat(format.toString(), equalTo(expected.toString()));
  }
}
