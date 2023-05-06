package net.bramp.ffmpeg.modelmapper;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.EncodingOptions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class FFmpegOutputBuilderToEncodingOptionsMapperTest {
  public static List<Field> params() {
    return Stream.concat(
            Arrays.stream(FFmpegOutputBuilder.class.getDeclaredFields()),
            Arrays.stream(FFmpegOutputBuilder.class.getSuperclass().getDeclaredFields()))
        // Ignore static fields, as they can't be included in options
        .filter(field -> !Modifier.isStatic(field.getModifiers()))
        // Ignore the parent, as it is not serialized
        .filter(field -> !field.getName().equals("parent"))
        // Ignore pass configuration. This is usually defined by the execution
        .filter(field -> !field.getName().startsWith("pass"))
        // Ignore throwWarnings configuration. Shouldn't override as it's not relevant for the
        // command
        .filter(field -> !field.getName().equals("throwWarnings"))
        // Filename and uri might point to files that don't exist on a later machine
        .filter(field -> !field.getName().equals("filename"))
        .filter(field -> !field.getName().equals("uri"))
        .toList();
  }

  @ParameterizedTest
  @MethodSource("params")
  public void mapperCanSetField(Field parameter) throws IllegalAccessException {
    EncodingOptions reference = new FFmpegOutputBuilder().buildOptions();
    FFmpegOutputBuilder source = new FFmpegOutputBuilder();
    parameter.setAccessible(true);
    Object value = MapperTestUtils.getSpecialValue(parameter.getType());

    if (parameter.getName().endsWith("Enabled")) {
      value = false;
    }

    parameter.set(source, value);

    EncodingOptions target = source.buildOptions();

    assertFalse(
        EqualsBuilder.reflectionEquals(target, reference),
        "Failed to set field " + parameter.getName());
  }
}
