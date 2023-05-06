package net.bramp.ffmpeg;

import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.bramp.ffmpeg.builder.ProcessOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple function that creates a Process with the arguments, and returns a BufferedReader reading
 * stdout
 *
 * @author bramp
 */
public class RunProcessFunction implements ProcessFunction {

  static final Logger LOG = LoggerFactory.getLogger(RunProcessFunction.class);

  File workingDirectory;

  @Override
  public Process run(List<String> args, ProcessOptions processOptions) throws IOException {
    checkNotNull(args, "Arguments must not be null");
    checkArgument(!args.isEmpty(), "No arguments specified");

    if (LOG.isInfoEnabled()) {
      LOG.info("{}", String.join(" ", args));
    }

    ProcessBuilder builder = new ProcessBuilder(args);
    if (workingDirectory != null) {
      builder.directory(workingDirectory);
    }

    builder.redirectError(processOptions.getErrorStreamRedirect());
    builder.redirectOutput(processOptions.getOutputStreamRedirect());
    builder.redirectErrorStream(processOptions.isRedirectErrorStreamToOutputStream());

    return builder.start();
  }

  public RunProcessFunction setWorkingDirectory(String workingDirectory) {
    this.workingDirectory = new File(workingDirectory);
    return this;
  }

  public RunProcessFunction setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
    return this;
  }
}
