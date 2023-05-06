package net.bramp.ffmpeg.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.apache.commons.lang3.math.Fraction;

public class FractionDeserializer extends JsonDeserializer<Fraction> {
  private final Fraction zeroByZero;
  private final Fraction divideByZero;

  public FractionDeserializer() {
    this(Fraction.ZERO, Fraction.ZERO);
  }

  public FractionDeserializer(Fraction zeroByZero, Fraction divideByZero) {
    this.zeroByZero = zeroByZero;
    this.divideByZero = divideByZero;
  }

  @Override
  public Fraction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    String value = p.getValueAsString();

    if (zeroByZero != null && value.equals("0/0")) {
      return zeroByZero;
    }

    if (divideByZero != null && value.endsWith("/0")) {
      return divideByZero;
    }

    return Fraction.getFraction(p.getValueAsString());
  }
}
