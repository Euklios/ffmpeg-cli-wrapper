package net.bramp.ffmpeg.builder;

import java.net.URI;
import java.util.List;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.options.EncodingOptions;
import org.apache.commons.lang3.NotImplementedException;

public class FFmpegInputBuilder extends AbstractFFmpegStreamBuilder<FFmpegInputBuilder> {
  private boolean readAtNativeFrameRate;

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

  public FFmpegInputBuilder readAtNativeFrameRate() {
    this.readAtNativeFrameRate = true;
    return this;
  }

  @Override
  public EncodingOptions buildOptions() {
    throw new NotImplementedException(
        "The function AbstractFFmpegStreamBuilder#buildOptions() has not yet been implemented for FFmpegInputBuilder");
  }

  @Override
  protected void addGlobalFlags(FFmpegBuilder parent, ImmutableListBuilder<String> args) {
    args.addFlagIf(readAtNativeFrameRate, "-re");
    super.addGlobalFlags(parent, args);
  }

  @Override
  protected List<String> buildFileNameArgument() {
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
