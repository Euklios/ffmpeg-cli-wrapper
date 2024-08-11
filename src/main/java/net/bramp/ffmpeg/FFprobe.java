package net.bramp.ffmpeg;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import net.bramp.ffmpeg.builder.FFprobeBuilder;
import net.bramp.ffmpeg.io.LoggingFilterReader;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wrapper around FFprobe
 *
 * @author bramp
 */
public class FFprobe extends FFcommon {

  static final Logger LOG = LoggerFactory.getLogger(FFprobe.class);

  static final String FFPROBE = "ffprobe";
  static final String DEFAULT_PATH = MoreObjects.firstNonNull(System.getenv("FFPROBE"), FFPROBE);

  static final Gson gson = FFmpegUtils.getGson();

  public FFprobe() throws IOException {
    this(DEFAULT_PATH, new RunProcessFunction());
  }

  public FFprobe(@Nonnull ProcessFunction runFunction) throws IOException {
    this(DEFAULT_PATH, runFunction);
  }

  public FFprobe(@Nonnull String path) throws IOException {
    this(path, new RunProcessFunction());
  }

  public FFprobe(@Nonnull String path, @Nonnull ProcessFunction runFunction) {
    super(path, runFunction);
  }

  /**
   * Returns true if the binary we are using is the true ffprobe. This is to avoid conflict with
   * avprobe (from the libav project), that some symlink to ffprobe.
   *
   * @return true iff this is the official ffprobe binary.
   * @throws IOException If a I/O error occurs while executing ffprobe.
   */
  public boolean isFFprobe() throws IOException {
    return version().startsWith("ffprobe");
  }

  /**
   * Throws an exception if this is an unsupported version of ffprobe.
   *
   * @throws IllegalArgumentException if this is not the official ffprobe binary.
   * @throws IOException If a I/O error occurs while executing ffprobe.
   */
  private void checkIfFFprobe() throws IllegalArgumentException, IOException {
    if (!isFFprobe()) {
      throw new IllegalArgumentException(
          "This binary '" + path + "' is not a supported version of ffprobe");
    }
  }

  @Override
  public void run(List<String> args) throws IOException {
    checkIfFFprobe();
    super.run(args);
  }

  @Override
  public Future<Void> runAsync(List<String> args) throws IOException {
    checkIfFFprobe();
    return super.runAsync(args);
  }

  @Override
  public Future<Void> runAsync(List<String> args, ExecutorService executor) throws IOException {
    checkIfFFprobe();
    return super.runAsync(args, executor);
  }

  public FFmpegProbeResult probe(String mediaPath) throws IOException {
    return probe(this.builder().setInput(mediaPath));
  }

  /**
   * Probes the given media and parses the result
   *
   * @param mediaPath Path or uri to the media file
   * @param userAgent The user agent string for web requests
   * @return FFmpegProbeResult
   * @throws IOException if the process communication fails
   * @deprecated Use {@link FFprobeBuilder#setUserAgent(String)} instead
   */
  @Deprecated
  public FFmpegProbeResult probe(String mediaPath, @Nullable String userAgent) throws IOException {
    return probe(this.builder().setInput(mediaPath).setUserAgent(userAgent));
  }

  public FFmpegProbeResult probe(FFprobeBuilder builder) throws IOException {
    return unwrapFutureException(probeAsync(builder));
  }

  public Future<FFmpegProbeResult> probeAsync(FFprobeBuilder builder) throws IOException {
    return probeAsync(builder, ForkJoinPool.commonPool());
  }

  public Future<FFmpegProbeResult> probeAsync(FFprobeBuilder builder, ExecutorService executor) throws IOException {
    checkNotNull(builder);
    return probeAsync(builder.build());
  }

  /**
   * Probes the given media and parses the result
   *
   * @param mediaPath Path or uri to the media file
   * @param userAgent The user agent string for web requests
   * @param extraArgs Additional arguments passed to ffprobe
   * @return FFmpegProbeResult
   * @throws IOException if the process communication fails
   * @deprecated Use {@link FFprobeBuilder#setUserAgent(String)} and {@link FFprobeBuilder#addExtraArgs(String...)} instead
   */
  @Deprecated
  public FFmpegProbeResult probe(String mediaPath, @Nullable String userAgent, @Nullable String... extraArgs) throws IOException {
    return probe(this.builder().setInput(mediaPath).setUserAgent(userAgent).addExtraArgs(extraArgs).build());
  }

  public FFmpegProbeResult probe(List<String> args) throws IOException {
    return unwrapFutureException(probeAsync(args));
  }

  public Future<FFmpegProbeResult> probeAsync(List<String> args) throws IOException {
    return probeAsync(args, ForkJoinPool.commonPool());
  }

  // TODO Add Probe Inputstream
  public Future<FFmpegProbeResult> probeAsync(List<String> args, ExecutorService executor) throws IOException {
    checkIfFFprobe();

    Process p = runFunc.run(path(args));

    return executor.submit(() -> {
      try {
        Reader reader = wrapInReader(p);
        if (LOG.isDebugEnabled()) {
          reader = new LoggingFilterReader(reader, LOG);
        }

        FFmpegProbeResult result = gson.fromJson(reader, FFmpegProbeResult.class);

        throwOnError(p, result);

        if (result == null) {
          throw new IllegalStateException("Gson returned null, which shouldn't happen :(");
        }

        return result;
      } catch (FFmpegException e) {
        throw new FFmpegAsyncException(e);
      } finally {
        p.destroy();
      }
    });
  }

  @CheckReturnValue
  public FFprobeBuilder builder() {
    return new FFprobeBuilder();
  }
}
