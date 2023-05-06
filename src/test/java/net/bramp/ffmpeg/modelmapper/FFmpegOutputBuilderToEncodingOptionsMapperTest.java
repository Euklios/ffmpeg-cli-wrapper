package net.bramp.ffmpeg.modelmapper;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.EncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class FFmpegOutputBuilderToEncodingOptionsMapperTest {
  public static List<Field> params() {
    return Arrays.stream(FFmpegOutputBuilder.class.getFields()).toList();
  }

  @ParameterizedTest
  @MethodSource("params")
  public void mapperCanSetField(Field parameter) throws IllegalAccessException {
    EncodingOptions reference = new FFmpegOutputBuilder().buildOptions();
    FFmpegOutputBuilder source = new FFmpegOutputBuilder();
    parameter.setAccessible(true);
    parameter.set(source, MapperTestUtils.getSpecialValue(parameter.getType()));

    EncodingOptions target = source.buildOptions();

    assertFalse(EqualsBuilder.reflectionEquals(target, reference));
  }
}
