package net.bramp.ffmpeg;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PreconditionsCheckValidStreamTest {
  public static List<URI> data() {
    return Arrays.asList(
        URI.create("udp://10.1.0.102:1234"),
        URI.create("tcp://127.0.0.1:2000"),
        URI.create("udp://236.0.0.1:2000"),
        URI.create("rtmp://live.twitch.tv/app/live_"),
        URI.create("rtmp:///live/myStream.sdp"),
        URI.create("rtp://127.0.0.1:1234"),
        URI.create("rtsp://localhost:8888/live.sdp"),
        URI.create("rtsp://localhost:8888/live.sdp?tcp"),

        // Some others
        URI.create("UDP://10.1.0.102:1234"));
  }

  @ParameterizedTest
  @MethodSource("data")
  public void testUri(URI uri) {
    Preconditions.checkValidStream(uri);
  }
}
