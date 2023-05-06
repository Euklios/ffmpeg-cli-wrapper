package net.bramp.ffmpeg.builder;

import net.bramp.ffmpeg.options.EncodingOptions;
import org.apache.commons.lang3.NotImplementedException;

import java.net.URI;
import java.util.List;

public class FFmpegInputBuilder extends AbstractFFmpegStreamBuilder<FFmpegInputBuilder> {
  public FFmpegInputBuilder() {
    super();
  }

  protected FFmpegInputBuilder(FFmpegBuilder parent, String filename) {
    super(parent, filename);
  }

  protected FFmpegInputBuilder(FFmpegBuilder parent, URI uri) {
    super(parent, uri);
  }
  @Override
  protected FFmpegInputBuilder getThis() {
    return this;
  }

  @Override
  public EncodingOptions buildOptions() {
  throw new NotImplementedException("The function AbstractFFmpegStreamBuilder#buildOptions() has not yet been implemented for FFmpegInputBuilder");
  }

  @Override
  protected List<String> buildFileNameArgument(int pass) {
    if (filename != null) {
      return List.of("-i", filename);
    } else if (uri != null) {
      return List.of("-i", uri.toString());
    } else {
      assert (false);
      return List.of();
    }
  }
}
