package net.bramp.ffmpeg.builder;

public class ProcessOptions {
  private boolean redirectErrorStreamToOutputStream = true;
  private ProcessBuilder.Redirect outputStreamRedirect = ProcessBuilder.Redirect.PIPE;
  private ProcessBuilder.Redirect errorStreamRedirect = ProcessBuilder.Redirect.PIPE;

  public boolean isRedirectErrorStreamToOutputStream() {
    return redirectErrorStreamToOutputStream;
  }

  public void setRedirectErrorStreamToOutputStream(boolean redirectErrorStreamToOutputStream) {
    this.redirectErrorStreamToOutputStream = redirectErrorStreamToOutputStream;
  }

  public ProcessBuilder.Redirect getOutputStreamRedirect() {
    return outputStreamRedirect;
  }

  public void setOutputStreamRedirect(ProcessBuilder.Redirect outputStreamRedirect) {
    this.outputStreamRedirect = outputStreamRedirect;
  }

  public ProcessBuilder.Redirect getErrorStreamRedirect() {
    return errorStreamRedirect;
  }

  public void setErrorStreamRedirect(ProcessBuilder.Redirect errorStreamRedirect) {
    this.errorStreamRedirect = errorStreamRedirect;
  }
}
