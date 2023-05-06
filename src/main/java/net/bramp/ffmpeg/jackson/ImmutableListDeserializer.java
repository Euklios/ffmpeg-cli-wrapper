package net.bramp.ffmpeg.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import java.io.IOException;
import java.util.*;

public class ImmutableListDeserializer extends CollectionDeserializer {
  public ImmutableListDeserializer(CollectionDeserializer src) {
    super(src);
  }

  @Override
  public List deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return List.copyOf(super.deserialize(p, ctxt));
  }

  @Override
  public List getNullValue(DeserializationContext ctxt) {
    return Collections.emptyList();
  }

  @Override
  public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property)
      throws JsonMappingException {
    return new ImmutableListDeserializer(super.createContextual(ctxt, property));
  }
}
