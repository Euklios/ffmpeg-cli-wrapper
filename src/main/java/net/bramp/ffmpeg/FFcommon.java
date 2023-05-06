package net.bramp.ffmpeg;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

import net.bramp.error.handler.ExceptionParser;
import net.bramp.ffmpeg.builder.ProcessOptions;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;

import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotNull;

/** Private class to contain common methods for both FFmpeg and FFprobe. */
abstract class FFcommon {

  /** Path to the binary (e.g. /usr/bin/ffmpeg) */
  final String path;

  /** Function to run FFmpeg. We define it like this so we can swap it out (during testing) */
  final ProcessFunction runFunc;

  /** Version string */
  String version = null;

  public FFcommon(@Nonnull String path) {
    this(path, new RunProcessFunction());
  }

  protected FFcommon(@Nonnull String path, @Nonnull ProcessFunction runFunction) {
    checkArgument(!Strings.isNullOrEmpty(path));
    this.runFunc = checkNotNull(runFunction);
    this.path = path;
  }

  protected BufferedReader wrapInReader(Process p) {
    return new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
  }

  protected void throwOnError(Process process, ProcessOptions processOptions) throws IOException {
    try {
      process.waitFor(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new IOException("Interrupted while waiting for " + path + " to finish");
    }

    if (process.exitValue() != 0) {
      throw ExceptionParser.loadException(process.info().command().orElse(""), processOptions);
    }
  }

  /**
   * Returns the version string for this binary.
   *
   * @return the version string.
   * @throws IOException If there is an error capturing output from the binary.
   */
  public synchronized @Nonnull String version() throws IOException {
    if (this.version == null) {
      ProcessOptions processOptions = new ProcessOptions();
      Process p = runFunc.run(List.of(path, "-version"), processOptions);
      try {
        BufferedReader r = wrapInReader(p);
        this.version = r.readLine();
        CharStreams.copy(r, CharStreams.nullWriter()); // Throw away rest of the output

        throwOnError(p, processOptions);
      } finally {
        p.destroy();
      }
    }
    return version;
  }

  public String getPath() {
    return path;
  }

  /**
   * Returns the full path to the binary with arguments appended.
   *
   * @param args The arguments to pass to the binary.
   * @return The full path and arguments to execute the binary.
   * @throws IOException If there is an error capturing output from the binary
   */
  public List<String> path(List<String> args) throws IOException {
    return new ImmutableListBuilder<String>().add(path).addAll(args).build();
  }

  /**
   * Runs the binary (ffmpeg) with the supplied args. Does not block the current execution.
   *
   * @param args The arguments to pass to the binary.
   * @throws IOException If there is a problem executing the binary.
   */
  public CompletableFuture<Process> asyncRun(List<String> args, ProcessOptions processOptions) throws IOException {
    return createAndStartProcess(args, processOptions).onExit();
  }

  /**
   * Runs the binary (ffmpeg) with the supplied args. Blocking until finished.
   *
   * @param args The arguments to pass to the binary.
   * @throws IOException If there is a problem executing the binary.
   */
  public void run(List<String> args, ProcessOptions processOptions) throws IOException {
    Process process = createAndStartProcess(args, processOptions);

    try {
      process.waitFor();
      throwOnError(process, processOptions);
    } catch (InterruptedException e) {
      // TODO: This should probably be thrown as a checked exception
      throw new RuntimeException(e);
    }
  }

  protected Process createAndStartProcess(List<String> args, ProcessOptions processOptions) throws IOException {
    checkNotNull(args);
    checkNotNull(processOptions);

    Process process = runFunc.run(path(args), processOptions);
    assert process != null;

    return process;
  }
}
