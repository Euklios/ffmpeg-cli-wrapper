package net.bramp.ffmpeg.progress;

import static net.bramp.ffmpeg.Helper.combineResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.List;
import net.bramp.ffmpeg.fixtures.Progresses;
import org.junit.jupiter.api.Test;

public class TcpProgressParserTest extends AbstractProgressParserTest {

  @Override
  public ProgressParser newParser(ProgressListener listener)
      throws IOException, URISyntaxException {
    return new TcpProgressParser(listener);
  }

  @Test
  public void testNormal() throws IOException, InterruptedException, URISyntaxException {
    parser.start();

    Socket client = new Socket(uri.getHost(), uri.getPort());
    assertTrue(client.isConnected(), "Socket is connected");

    InputStream inputStream = combineResource(Progresses.allFiles);
    OutputStream outputStream = client.getOutputStream();

    long bytes = ByteStreams.copy(inputStream, outputStream);

    // HACK, but give the TcpProgressParser thread time to actually handle the connection/data
    // before the client is closed, and the parser is stopped.
    Thread.sleep(100);

    client.close();
    parser.stop();

    assertThat(bytes, greaterThan(0L));
    assertThat(progesses, equalTo((List<Progress>) Progresses.allProgresses));
  }

  @Test
  public void testPrematureDisconnect()
      throws IOException, InterruptedException, URISyntaxException {
    parser.start();
    new Socket(uri.getHost(), uri.getPort()).close();
    parser.stop();

    assertTrue(progesses.isEmpty());
  }
}
