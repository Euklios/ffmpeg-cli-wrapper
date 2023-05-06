package net.bramp.ffmpeg.options;

/**
 * @author bramp
 */
public record EncodingOptions(
    MainEncodingOptions main, AudioEncodingOptions audio, VideoEncodingOptions video) {

  /**
   * @deprecated Use {@link EncodingOptions#main()} instead
   */
  @Deprecated(forRemoval = true)
  public MainEncodingOptions getMain() {
    return main();
  }

  /**
   * @deprecated Use {@link EncodingOptions#audio()} instead
   */
  @Deprecated(forRemoval = true)
  public AudioEncodingOptions getAudio() {
    return audio();
  }

  /**
   * @deprecated Use {@link EncodingOptions#video()} instead
   */
  @Deprecated(forRemoval = true)
  public VideoEncodingOptions getVideo() {
    return video();
  }
}
