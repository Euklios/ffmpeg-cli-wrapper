package net.bramp.ffmpeg.progress;

import static net.bramp.ffmpeg.Helper.combineResource;
import static net.bramp.ffmpeg.Helper.loadResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.fixtures.Progresses;
import net.bramp.ffmpeg.fixtures.Samples;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.junit.Test;

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
    assertTrue("Socket is connected", client.isConnected());

    InputStream inputStream = combineResource(Progresses.allFiles);
    OutputStream outputStream = client.getOutputStream();

    long bytes = ByteStreams.copy(inputStream, outputStream);

    // HACK, but give the TcpProgressParser thread time to actually handle the connection/data
    // before the client is closed, and the parser is stopped.
    Thread.sleep(100);

    client.close();
    parser.stop();

    assertThat(bytes, greaterThan(0L));
    assertThat(progesses, equalTo(Progresses.allProgresses));
  }



  @Test
  public void testNaProgressPackets() throws IOException, InterruptedException, URISyntaxException {
    parser.start();

    Socket client = new Socket(uri.getHost(), uri.getPort());
    assertTrue("Socket is connected", client.isConnected());

    InputStream inputStream = combineResource(Progresses.naProgressFile);
    OutputStream outputStream = client.getOutputStream();

    long bytes = ByteStreams.copy(inputStream, outputStream);

    // HACK, but give the TcpProgressParser thread time to actually handle the connection/data
    // before the client is closed, and the parser is stopped.
    Thread.sleep(100);

    client.close();
    parser.stop();

    assertThat(bytes, greaterThan(0L));
    assertThat(progesses, equalTo(Progresses.naProgresses));
  }

  @Test
  public void testPrematureDisconnect()
      throws IOException {
    parser.start();
    new Socket(uri.getHost(), uri.getPort()).close();
    parser.stop();

    assertTrue(progesses.isEmpty());
  }

  @Test
  public void testIssue332() throws IOException {
    String inputPath = Samples.big_buck_bunny_720p_1mb;
    String outputPath = Samples.output_mp4;
    long byteRate = 1000;

    FFmpeg ffmpeg = new FFmpeg();
    FFprobe ffprobe = new FFprobe();

    FFmpegProbeResult inputProbe = ffprobe.probe(inputPath);

    FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(inputPath)
            .overrideOutputFiles(true)
            .addOutput(outputPath)
            .setAudioBitRate(byteRate * 1024)
            .done();

    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

    List<Double> percentages = new ArrayList<>();

    FFmpegJob job = executor.createJob(builder, (progress) -> {
      double duraiton_ns = inputProbe.getFormat().duration * TimeUnit.SECONDS.toNanos(1);
      double percentage = progress.out_time_ns / duraiton_ns;

      percentages.add(percentage);
    });

    job.run();

    assertEquals(FFmpegJob.State.FINISHED, job.getState());
    assertFalse(percentages.isEmpty());
  }
}
