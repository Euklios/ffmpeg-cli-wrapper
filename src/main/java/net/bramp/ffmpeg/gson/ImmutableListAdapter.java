package net.bramp.ffmpeg.gson;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ImmutableListAdapter implements JsonDeserializer<List<?>> {
  @Override
  public List<?> deserialize(
      final JsonElement json, final Type type, final JsonDeserializationContext context)
      throws JsonParseException {
    final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
    final Type parameterizedType = listTypeOf(typeArguments[0]).getType();
    final List<?> list = context.deserialize(json, parameterizedType);

    return List.copyOf(list);
  }

  private static <T> TypeToken<List<T>> listTypeOf(final Type entry) {
    return new TypeToken<List<T>>() {}.where(
        new TypeParameter<>() {}, (TypeToken<T>) TypeToken.of(entry));
  }
}
