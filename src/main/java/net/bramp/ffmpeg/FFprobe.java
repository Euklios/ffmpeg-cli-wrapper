package net.bramp.ffmpeg;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.bramp.ffmpeg.builder.ProcessOptions;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.io.LoggingFilterReader;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public FFprobe(@Nonnull String path, @Nonnull ProcessFunction runFunction) throws IOException {
    super(path, runFunction);
    version();
  }

  public FFmpegProbeResult probe(String mediaPath) throws IOException {
    return probe(mediaPath, null);
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
  public void run(List<String> args, ProcessOptions processOptions) throws IOException {
    checkIfFFprobe();
    super.run(args, processOptions);
  }

  // TODO Add Probe Inputstream
  public FFmpegProbeResult probe(String mediaPath, @Nullable String userAgent) throws IOException {
    checkIfFFprobe();

    ImmutableListBuilder<String> args = new ImmutableListBuilder<>();

    // TODO Add:
    // .add("--show_packets")
    // .add("--show_frames")

    args.add(path);
    args.add("-v", "quiet");
    args.addArgIf(userAgent != null, "-user_agent", userAgent);

    args.add("-print_format", "json");
    args.add("-show_error");
    args.add("-show_format");
    args.add("-show_streams");
    args.add("-show_chapters");
    args.add(mediaPath);

    ProcessOptions processOptions = new ProcessOptions();
    Process p = runFunc.run(args.build(), processOptions);
    try {
      Reader reader = wrapInReader(p);
      if (LOG.isDebugEnabled()) {
        reader = new LoggingFilterReader(reader, LOG);
      }

      FFmpegProbeResult result = gson.fromJson(reader, FFmpegProbeResult.class);

      throwOnError(p, processOptions);

      if (result == null) {
        throw new IllegalStateException("Gson returned null, which shouldn't happen :(");
      }

      return result;

    } finally {
      p.destroy();
    }
  }
}
