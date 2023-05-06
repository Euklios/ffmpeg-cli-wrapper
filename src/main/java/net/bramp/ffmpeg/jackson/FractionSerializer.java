package net.bramp.ffmpeg.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.apache.commons.lang3.math.Fraction;

public class FractionSerializer extends JsonSerializer<Fraction> {
  @Override
  public void serialize(Fraction value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value.getDenominator() == 1) {
      gen.writeString(Integer.toString(value.getNumerator()));
      return;
    }

    gen.writeString(value.toProperString());
  }
}
