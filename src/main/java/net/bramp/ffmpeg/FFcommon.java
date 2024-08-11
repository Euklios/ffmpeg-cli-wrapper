package net.bramp.ffmpeg;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import net.bramp.ffmpeg.io.ProcessUtils;
import net.bramp.ffmpeg.probe.FFmpegError;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;

/** Private class to contain common methods for both FFmpeg and FFprobe. */
abstract class FFcommon {

  /** Path to the binary (e.g. /usr/bin/ffmpeg) */
  final String path;

  /** Function to run FFmpeg. We define it like this so we can swap it out (during testing) */
  final ProcessFunction runFunc;

  /** Version string */
  String version = null;

  /** Process input stream */
  Appendable processOutputStream = System.out;

  /** Process error stream */
  Appendable processErrorStream = System.err;

  public FFcommon(@Nonnull String path) {
    this(path, new RunProcessFunction());
  }

  protected FFcommon(@Nonnull String path, @Nonnull ProcessFunction runFunction) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(path));
    this.runFunc = checkNotNull(runFunction);
    this.path = path;
  }

  public void setProcessOutputStream(@Nonnull Appendable processOutputStream) {
    Preconditions.checkNotNull(processOutputStream);
    this.processOutputStream = processOutputStream;
  }

  public void setProcessErrorStream(@Nonnull Appendable processErrorStream) {
    Preconditions.checkNotNull(processErrorStream);
    this.processErrorStream = processErrorStream;
  }

  private BufferedReader _wrapInReader(final InputStream inputStream) {
    return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
  }

  protected BufferedReader wrapInReader(Process p) {
    return _wrapInReader(p.getInputStream());
  }

  protected void throwOnError(Process p) throws IOException {
    try {
      if (ProcessUtils.waitForWithTimeout(p, 1, TimeUnit.SECONDS) != 0) {
        // TODO Parse the error
        throw new IOException(path + " returned non-zero exit status. Check stdout.");
      }
    } catch (TimeoutException e) {
      throw new IOException("Timed out waiting for " + path + " to finish.");
    }
  }

  protected void throwOnError(Process p, FFmpegProbeResult result) throws IOException {
    try {
      if (ProcessUtils.waitForWithTimeout(p, 1, TimeUnit.SECONDS) != 0) {
        // TODO Parse the error
        final FFmpegError ffmpegError = null == result ? null : result.getError();
        throw new FFmpegException(
                path + " returned non-zero exit status. Check stdout.", ffmpegError);
      }
    } catch (TimeoutException e) {
      throw new IOException("Timed out waiting for " + path + " to finish.");
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
      Process p = runFunc.run(ImmutableList.of(path, "-version"));
      try {
        BufferedReader r = wrapInReader(p);
        this.version = r.readLine();
        CharStreams.copy(r, CharStreams.nullWriter()); // Throw away rest of the output

        throwOnError(p);
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
    return ImmutableList.<String>builder().add(path).addAll(args).build();
  }

  /**
   * Runs the binary (ffmpeg) with the supplied args. Blocking until finished.
   *
   * @param args The arguments to pass to the binary.
   * @throws IOException If there is a problem executing the binary.
   */
  public void run(List<String> args) throws IOException {
    unwrapFutureException(runAsync(args));
  }

  protected <T> T unwrapFutureException(Future<T> tFuture) throws IOException {
    try {
      return tFuture.get();
    } catch (ExecutionException e) {
      unwrapException(e);
      throw new IllegalStateException("Expected exception to be thrown, but wasn't", e);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
    }
  }

  protected void unwrapException(Throwable e) throws IOException {
    if (e instanceof IOException) {
      throw (IOException) e;
    }

    if (e instanceof FFmpegAsyncException) {
      unwrapException(e.getCause());
    }

    if (e instanceof RuntimeException) {
      throw (RuntimeException) e;
    }

    if (e instanceof ExecutionException) {
      unwrapException(e.getCause());
    }

    throw new RuntimeException(e);
  }

  public Future<Void> runAsync(List<String> args) throws IOException {
    return this.runAsync(args, ForkJoinPool.commonPool());
  }

  public Future<Void> runAsync(List<String> args, ExecutorService executor) throws IOException {
    requireNonNull(args);

    CompletableFuture<Void> future = new CompletableFuture<>();
    final AtomicReference<Process> processRef = new AtomicReference<>();

    try {
      Process p = runFunc.run(path(args));
      assert (p != null);
      processRef.set(p);
    } catch (IOException e) {
      future.completeExceptionally(e);
      return future;
    }

    Future<?> taskFuture = executor.submit(() -> {
      try {
        Process p = processRef.get();
        CompletableFuture<Void> outputCopy = copyInputStreamAsync(p.getInputStream(), processOutputStream, executor);
        CompletableFuture<Void> errorCopy = copyInputStreamAsync(p.getErrorStream(), processErrorStream, executor);

        CompletableFuture.allOf(outputCopy, errorCopy).join();
        throwOnError(p);

        future.complete(null);

      } catch (IOException e) {
        future.completeExceptionally(e);
      } finally {
        Process p = processRef.get();
        p.destroy();
      }
    });

    return future.whenComplete((result, throwable) -> {
      if (future.isCancelled()) {
        Process p = processRef.get();
        p.destroy();
        taskFuture.cancel(true);
      }
    });
  }

  protected CompletableFuture<Void> copyInputStreamAsync(InputStream inStream, Appendable outStream, ExecutorService executor) {
    return CompletableFuture.runAsync(() -> {
      try {
        CharStreams.copy(_wrapInReader(inStream), outStream);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }, executor);
  }
}
