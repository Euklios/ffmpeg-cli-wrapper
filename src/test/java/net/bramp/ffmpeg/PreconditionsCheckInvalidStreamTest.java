package net.bramp.ffmpeg;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PreconditionsCheckInvalidStreamTest {
  public static List<URI> data() {
    return Arrays.asList(
        // Illegal schemes
        URI.create("http://www.example.com/"),
        URI.create("https://live.twitch.tv/app/live_"),
        URI.create("ftp://236.0.0.1:2000"),

        // Missing ports
        URI.create("udp://10.1.0.102/"),
        URI.create("tcp://127.0.0.1/"));
  }

  @ParameterizedTest
  @MethodSource("data")
  public void testUri(URI uri) {
    assertThrows(IllegalArgumentException.class, () -> Preconditions.checkValidStream(uri));
  }
}
