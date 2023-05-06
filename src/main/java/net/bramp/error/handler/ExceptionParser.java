package net.bramp.error.handler;

import net.bramp.ffmpeg.builder.ProcessOptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class ExceptionParser {
  public static IOException loadException(String path, ProcessOptions processOptions) {
    if (processOptions.isRedirectErrorStreamToOutputStream()) {
        return genericFallbackError(path);
    }

      ProcessBuilder.Redirect redirect = processOptions.getErrorStreamRedirect();

      if (redirect.file() != null) {
        return loadExceptionFromFile(path, redirect.file());
      }

      return genericFallbackError(path);
  }

  private static IOException genericFallbackError(String path) {
    return new IOException(path + " returned non-zero exit status.");
  }

  private static IOException loadExceptionFromFile(String path, File file) {
    String fileContent;
    try {
      fileContent = String.join("\n", Files.readAllLines(file.toPath(), Charset.defaultCharset()));
    } catch (IOException ignored) {
      return genericFallbackError(path);
    }

    return new IOException(path + " returned non-zero exit status.\n" + fileContent);
  }
}
