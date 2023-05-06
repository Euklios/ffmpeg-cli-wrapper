package net.bramp.ffmpeg.modelmapper;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class VideoEncodingOptionsToFFmpegOutputBuilderMapperTest {
  public static List<Parameter> params() {
    return Arrays.stream(VideoEncodingOptions.class.getConstructors()[0].getParameters()).toList();
  }

  @ParameterizedTest
  @MethodSource("params")
  public void mapperCanSetField(Parameter parameter)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    FFmpegOutputBuilder reference = new FFmpegOutputBuilder();
    FFmpegOutputBuilder target = new FFmpegOutputBuilder();
    VideoEncodingOptions source =
        MapperTestUtils.instantiateObjectWithDefaultValues(VideoEncodingOptions.class, parameter);

    Mapper.map(source, target);

    assertFalse(EqualsBuilder.reflectionEquals(target, reference));
  }
}
