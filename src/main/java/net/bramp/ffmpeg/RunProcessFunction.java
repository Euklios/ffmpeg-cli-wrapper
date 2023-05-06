package net.bramp.ffmpeg;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
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
    Preconditions.checkNotNull(args, "Arguments must not be null");
    Preconditions.checkArgument(!args.isEmpty(), "No arguments specified");

    if (LOG.isInfoEnabled()) {
      LOG.info("{}", Joiner.on(" ").join(args));
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
