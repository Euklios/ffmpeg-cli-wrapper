package net.bramp.ffmpeg.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class FormatDecimalIntegerTest {
  public static List<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {0.0, "0"},
          {1.0, "1"},
          {-1.0, "-1"},
          {0.1, "0.1"},
          {1.1, "1.1"},
          {1.10, "1.1"},
          {1.001, "1.001"},
          {100, "100"},
          {100.01, "100.01"},
        });
  }

  @ParameterizedTest
  @MethodSource("data")
  public void formatDecimalInteger(double input, String expected) throws Exception {
    String got = FFmpegOutputBuilder.formatDecimalInteger(input);

    assertThat(got, equalTo(expected));
  }
}
