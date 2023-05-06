package net.bramp.ffmpeg.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class ImmutableMapDeserializer extends MapDeserializer {
  public ImmutableMapDeserializer(MapDeserializer src) {
    super(src);
  }

  @Override
  public Map deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return Map.copyOf(super.deserialize(p, ctxt));
  }

  @Override
  public Map getNullValue(DeserializationContext ctxt) {
    return Collections.emptyMap();
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
      throws JsonMappingException {
    return new ImmutableMapDeserializer((MapDeserializer) super.createContextual(ctxt, property));
  }
}
