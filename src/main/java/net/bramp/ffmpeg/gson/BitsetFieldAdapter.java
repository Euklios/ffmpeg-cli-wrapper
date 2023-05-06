package net.bramp.ffmpeg.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class BitsetFieldAdapter extends TypeAdapter<Boolean> {
  @Override
  public void write(JsonWriter out, Boolean value) throws IOException {
    out.value(value);
  }

  @Override
  public Boolean read(JsonReader in) throws IOException {
    return switch (in.peek()) {
      case NUMBER -> in.nextInt() != 0;
      case BOOLEAN -> in.nextBoolean();
      default -> throw new IllegalStateException("Unexpected token " + in.peek().name());
    };
  }
}
