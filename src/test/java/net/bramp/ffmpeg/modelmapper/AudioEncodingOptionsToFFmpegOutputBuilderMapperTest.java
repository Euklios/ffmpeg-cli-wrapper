package net.bramp.ffmpeg.modelmapper;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.AudioEncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AudioEncodingOptionsToFFmpegOutputBuilderMapperTest {
  private final Parameter parameter;

  public AudioEncodingOptionsToFFmpegOutputBuilderMapperTest(Parameter parameter) {
    this.parameter = parameter;
  }

  @Parameterized.Parameters(name = "[0]: {0}")
  public static List<Parameter> params() {
    return Arrays.stream(AudioEncodingOptions.class.getConstructors()[0].getParameters()).toList();
  }

  @Test
  public void mapperCanSetField()
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    FFmpegOutputBuilder reference = new FFmpegOutputBuilder();
    FFmpegOutputBuilder target = new FFmpegOutputBuilder();
    AudioEncodingOptions source =
        MapperTestUtils.instantiateObjectWithDefaultValues(AudioEncodingOptions.class, parameter);

    Mapper.map(source, target);

    assertFalse(EqualsBuilder.reflectionEquals(target, reference));
  }
}
