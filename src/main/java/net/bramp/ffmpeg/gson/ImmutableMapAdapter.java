package net.bramp.ffmpeg.gson;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class ImmutableMapAdapter implements JsonDeserializer<Map<?, ?>> {
    @Override
    public Map<?, ?> deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
        final Type parameterizedType = mapTypeOf(typeArguments[0], typeArguments[1]).getType();
        final Map<?, ?> map = context.deserialize(json, parameterizedType);

        return Map.copyOf(map);
    }

    private static <K, V> TypeToken<Map<K, V>> mapTypeOf(final Type key, final Type value) {
        return new TypeToken<Map<K, V>>() {}
                .where(new TypeParameter<>() {}, (TypeToken<K>) TypeToken.of(key))
                .where(new TypeParameter<>() {}, (TypeToken<V>) TypeToken.of(value));
    }
}
