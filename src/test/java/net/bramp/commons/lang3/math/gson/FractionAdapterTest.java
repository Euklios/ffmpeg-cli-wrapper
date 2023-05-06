package net.bramp.commons.lang3.math.gson;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import net.bramp.ffmpeg.jackson.FractionDeserializer;
import net.bramp.ffmpeg.jackson.FractionSerializer;
import org.apache.commons.lang3.math.Fraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FractionAdapterTest {
  static ObjectMapper objectMapper;

  @BeforeAll
  public static void setupGson() {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Fraction.class, new FractionDeserializer());
    module.addSerializer(Fraction.class, new FractionSerializer());

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(module);
  }

  private record TestData(String s, Fraction f) {}

  static final List<TestData> readTests =
      List.of(
          new TestData("null", null),
          new TestData("1", Fraction.getFraction(1, 1)),
          new TestData("1.0", Fraction.getFraction(1, 1)),
          new TestData("2", Fraction.getFraction(2, 1)),
          new TestData("0.5", Fraction.getFraction(1, 2)),
          new TestData("\"1\"", Fraction.getFraction(1, 1)),
          new TestData("\"1.0\"", Fraction.getFraction(1, 1)),
          new TestData("\"2\"", Fraction.getFraction(2, 1)),
          new TestData("\"0.5\"", Fraction.getFraction(1, 2)),
          new TestData("\"1/2\"", Fraction.getFraction(1, 2)),
          new TestData("\"1 1/2\"", Fraction.getFraction(1, 1, 2)));

  // Divide by zero
  static final List<TestData> zerosTests =
      List.of(new TestData("\"0/0\"", Fraction.ZERO), new TestData("\"1/0\"", Fraction.ZERO));

  static final List<TestData> writeTests =
      List.of(
          new TestData("0", Fraction.ZERO),
          new TestData("1", Fraction.getFraction(1, 1)),
          new TestData("2", Fraction.getFraction(2, 1)),
          new TestData("1/2", Fraction.getFraction(1, 2)),
          new TestData("1 1/2", Fraction.getFraction(1, 1, 2)));

  @Test
  public void testRead() throws JsonProcessingException {
    for (TestData test : readTests) {
      Fraction f = objectMapper.readValue(test.s, Fraction.class);
      assertThat(f, equalTo(test.f));
    }
  }

  @Test
  public void testZerosRead() throws JsonProcessingException {
    for (TestData test : zerosTests) {
      Fraction f = objectMapper.readValue(test.s, Fraction.class);
      assertThat(f, equalTo(test.f));
    }
  }

  @Test
  public void testWrites() throws JsonProcessingException {
    for (TestData test : writeTests) {
      String json = objectMapper.writeValueAsString(test.f);
      assertThat(json, equalTo('"' + test.s + '"'));
    }
  }

  @Test
  public void testWriteNull() throws JsonProcessingException {
    String json = objectMapper.writeValueAsString(null);
    assertThat(json, equalTo("null"));
  }
}
