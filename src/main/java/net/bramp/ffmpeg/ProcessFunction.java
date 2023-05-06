package net.bramp.ffmpeg;

import net.bramp.ffmpeg.builder.ProcessOptions;

import java.io.IOException;
import java.util.List;

/**
 * Creates and starts a process, returning an object representing the newly spawned process
 *
 * @author bramp
 */
public interface ProcessFunction {
  Process run(List<String> args, ProcessOptions processOptions) throws IOException;
}
