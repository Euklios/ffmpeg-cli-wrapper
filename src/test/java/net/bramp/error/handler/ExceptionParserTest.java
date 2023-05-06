package net.bramp.error.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import net.bramp.ffmpeg.builder.ProcessOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionParserTest {
  @Test
  public void testReturnsGenericExceptionIfErrorStreamRedirectsToOutput() {
    ProcessOptions processOptions = new ProcessOptions();
    processOptions.setRedirectErrorStreamToOutputStream(true);

    assertEquals(
        "/path/to/ffmpeg returned non-zero exit status.",
        ExceptionParser.loadException("/path/to/ffmpeg", processOptions).getMessage());
  }

  @Test
  public void testReturnsGenericExceptionIfErrorStreamDoesNotRedirectToFile() {
    ProcessOptions processOptions = new ProcessOptions();
    processOptions.setRedirectErrorStreamToOutputStream(false);
    processOptions.setErrorStreamRedirect(ProcessBuilder.Redirect.PIPE);

    assertEquals(
        "/path/to/ffmpeg returned non-zero exit status.",
        ExceptionParser.loadException("/path/to/ffmpeg", processOptions).getMessage());
  }

  @Test
  public void testReturnsFileContentIfErrorStreamRedirectsToFile() throws IOException {
    File tmpFile = File.createTempFile("test-", ".txt");

    try {
      ProcessOptions processOptions = new ProcessOptions();
      processOptions.setRedirectErrorStreamToOutputStream(false);
      processOptions.setErrorStreamRedirect(ProcessBuilder.Redirect.to(tmpFile));

      Files.writeString(tmpFile.toPath(), "This is a test message");

      assertEquals(
          "/path/to/ffmpeg returned non-zero exit status.\nThis is a test message",
          ExceptionParser.loadException("/path/to/ffmpeg", processOptions).getMessage());
    } finally {
      tmpFile.deleteOnExit();
    }
  }

  @Test
  public void testReturnsGenericExceptionIfErrorStreamFilesToCreateFile() throws IOException {
    File tmpFile = File.createTempFile("test-", ".txt");
    if (!tmpFile.delete()) {
      fail("Failed to delete the temporary file before testing");
    }

    try {
      ProcessOptions processOptions = new ProcessOptions();
      processOptions.setRedirectErrorStreamToOutputStream(false);
      processOptions.setErrorStreamRedirect(ProcessBuilder.Redirect.to(tmpFile));

      assertEquals(
          "/path/to/ffmpeg returned non-zero exit status.",
          ExceptionParser.loadException("/path/to/ffmpeg", processOptions).getMessage());
    } finally {
      tmpFile.deleteOnExit();
    }
  }
}
