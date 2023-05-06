package net.bramp.ffmpeg.gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BitsetFieldAdapterTest {

  private final BitsetFieldAdapter adapter = new BitsetFieldAdapter();

  @Test
  public void testWrite() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    adapter.write(jsonWriter, true);

    assertEquals("true", stringWriter.toString());
  }

  @Test
  public void testRead_numberTrue() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("1"));

    boolean result = adapter.read(jsonReader);

    assertTrue(result);
  }

  @Test
  public void testRead_numberFalse() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("0"));

    boolean result = adapter.read(jsonReader);

    assertFalse(result);
  }

  @Test
  public void testRead_booleanTrue() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("true"));

    boolean result = adapter.read(jsonReader);

    assertTrue(result);
  }

  @Test
  public void testRead_booleanFalse() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("false"));

    boolean result = adapter.read(jsonReader);

    assertFalse(result);
  }

  @Test
  public void testRead_unexpectedToken() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);

    assertThrows(IllegalStateException.class, () -> adapter.read(jsonReader));

    verify(jsonReader, times(2)).peek();
  }
}
