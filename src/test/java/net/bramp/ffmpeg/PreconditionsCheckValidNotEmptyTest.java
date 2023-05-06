package net.bramp.ffmpeg;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PreconditionsCheckValidNotEmptyTest {
  public static List<String> data() {
    return Arrays.asList("bob", " hello ");
  }

  @ParameterizedTest
  @MethodSource("data")
  public void testUri(String input) {
    Preconditions.checkNotEmpty(input, "test must not throw exception");
  }
}
