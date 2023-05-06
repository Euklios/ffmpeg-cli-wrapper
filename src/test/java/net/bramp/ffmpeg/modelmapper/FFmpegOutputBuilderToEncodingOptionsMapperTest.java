package net.bramp.ffmpeg.modelmapper;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.EncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class FFmpegOutputBuilderToEncodingOptionsMapperTest {
  private final Field parameter;

  public FFmpegOutputBuilderToEncodingOptionsMapperTest(Field parameter) {
    this.parameter = parameter;
  }

  @Parameterized.Parameters(name = "[0]: {0}")
  public static List<Field> params() {
    return Arrays.stream(FFmpegOutputBuilder.class.getFields()).toList();
  }

  @Test
  public void mapperCanSetField() throws IllegalAccessException {
    EncodingOptions reference = new FFmpegOutputBuilder().buildOptions();
    FFmpegOutputBuilder source = new FFmpegOutputBuilder();
    parameter.setAccessible(true);
    parameter.set(source, MapperTestUtils.getSpecialValue(parameter.getType()));

    EncodingOptions target = source.buildOptions();

    assertFalse(EqualsBuilder.reflectionEquals(target, reference));
  }
}
