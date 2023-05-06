package net.bramp.ffmpeg.modelmapper;

import com.google.common.base.Defaults;
import com.google.errorprone.annotations.Immutable;
import org.modelmapper.Condition;
import org.modelmapper.spi.MappingContext;

import java.util.Objects;

/**
 * Only maps properties which are not their type's default value.
 *
 * @param <S> source type
 * @param <D> destination type
 * @author bramp
 */
@Immutable
public class NotDefaultCondition<S, D> implements Condition<S, D> {

  public static final NotDefaultCondition<Object, Object> notDefault = new NotDefaultCondition<>();

  @Override
  public boolean applies(MappingContext<S, D> context) {
    return !Objects.equals(context.getSource(), Defaults.defaultValue(context.getSourceType()));
  }
}
