package net.bramp.ffmpeg;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PreconditionsCheckInvalidNotEmptyTest {
  public static List<String> data() {
    return Arrays.asList(null, "", "   ", "\n", " \n ");
  }

  @ParameterizedTest
  @MethodSource("data")
  public void testUri(String input) {
    assertThrows(
        IllegalArgumentException.class,
        () -> Preconditions.checkNotEmpty(input, "test must throw exception"));
  }
}
