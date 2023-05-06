package net.bramp.ffmpeg;

import java.io.IOException;
import java.util.List;
import net.bramp.ffmpeg.builder.ProcessOptions;

/**
 * Creates and starts a process, returning an object representing the newly spawned process
 *
 * @author bramp
 */
public interface ProcessFunction {
  Process run(List<String> args, ProcessOptions processOptions) throws IOException;
}
