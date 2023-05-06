package net.bramp.ffmpeg.modelmapper;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class MainEncodingOptionsToFFmpegOutputBuilderMapperTest {
  public static List<Parameter> params() {
    return Arrays.stream(MainEncodingOptions.class.getConstructors()[0].getParameters()).toList();
  }

  @ParameterizedTest
  @MethodSource("params")
  public void mapperCanSetField(Parameter parameter)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    FFmpegOutputBuilder reference = new FFmpegOutputBuilder();
    FFmpegOutputBuilder target = new FFmpegOutputBuilder();
    MainEncodingOptions source =
        MapperTestUtils.instantiateObjectWithDefaultValues(MainEncodingOptions.class, parameter);

    Mapper.map(source, target);

    assertFalse(EqualsBuilder.reflectionEquals(target, reference));
  }
}
