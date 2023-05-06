package net.bramp.ffmpeg.modelmapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.bramp.ffmpeg.builder.AbstractFFmpegStreamBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.*;

/**
 * Copies values from one type of object to another
 *
 * @author bramp
 */
public class Mapper {

  private Mapper() {
    throw new InstantiationError("Must not instantiate this class");
  }

  public static EncodingOptions toOptions(FFmpegOutputBuilder outputBuilder) {
    return new EncodingOptions(
        new MainEncodingOptions(
            outputBuilder.getFormat(),
            outputBuilder.getStartOffset(),
            outputBuilder.getDuration(),
            outputBuilder.getComplexFilter(),
            List.copyOf(outputBuilder.getMetaTags()),
            outputBuilder.getPreset(),
            outputBuilder.getPresetFilename(),
            List.copyOf(outputBuilder.getExtraArgs()),
            outputBuilder.getStrict(),
            outputBuilder.getTargetSize()),
        new AudioEncodingOptions(
            outputBuilder.isAudioEnabled(),
            outputBuilder.getAudioCodec(),
            outputBuilder.getAudioChannels(),
            outputBuilder.getAudioSampleRate(),
            outputBuilder.getAudioSampleFormat(),
            outputBuilder.getAudioBitRate(),
            outputBuilder.getAudioQuality(),
            outputBuilder.getAudioPreset(),
            outputBuilder.getAudioBitStreamFilter(),
            outputBuilder.getAudioFilter()),
        new VideoEncodingOptions(
            outputBuilder.isVideoEnabled(),
            outputBuilder.getVideoCodec(),
            outputBuilder.getVideoFrameRate(),
            outputBuilder.getVideoWidth(),
            outputBuilder.getVideoHeight(),
            outputBuilder.getVideoBitRate(),
            outputBuilder.getVideoFrames(),
            outputBuilder.getVideoFilter(),
            outputBuilder.getVideoPreset(),
            outputBuilder.getVideoSize(),
            outputBuilder.getVideoMovflags(),
            outputBuilder.getVideoPixelFormat(),
            outputBuilder.getConstantRateFactor(),
            outputBuilder.getVideoQuality(),
            outputBuilder.getVideoBitStreamFilter(),
            outputBuilder.isVideoCopyinkf()),
        new SubtitleEncodingOptions(
            outputBuilder.isSubtitleEnabled(),
            outputBuilder.getSubtitlePreset(),
            outputBuilder.getSubtitleCodec()));
  }

  public static void map(MainEncodingOptions opts, FFmpegOutputBuilder dest) {
    setIfNotNull(opts.duration(), (duration) -> dest.setDuration(duration, TimeUnit.MILLISECONDS));
    setIfNotNull(opts.format(), dest::setFormat);
    setIfNotNull(
        opts.startOffset(),
        (startOffset) -> dest.setStartOffset(startOffset, TimeUnit.MILLISECONDS));
    setIfNotNull(opts.complexFilter(), dest::setComplexFilter);

    // setIfNotNull(opts.metaTags(), strings -> strings.stream().filter(s -> s != null).forEach(s ->
    // dest.addM));
    setIfNotNull(opts.preset(), dest::setPreset);
    setIfNotNull(opts.presetFilename(), dest::setPresetFilename);
    setIfNotNull(opts.strict(), dest::setStrict);
    setConditional(opts.targetSize(), targetSize -> targetSize > 0, dest::setTargetSize);

    if (opts.metaTags() != null && opts.metaTags().size() != 0) {
      mapMetaTags(opts, dest);
    }

    if (opts.extraArgs() != null && opts.extraArgs().size() > 0) {
      dest.addExtraArgs(opts.extraArgs().toArray(String[]::new));
    }
  }

  private static void mapMetaTags(MainEncodingOptions opts, FFmpegOutputBuilder dest) {
    // TODO: I'm currently unable to do it without reflection, without changing the metaTags storage
    // model
    try {
      Field metaTagsField = AbstractFFmpegStreamBuilder.class.getDeclaredField("metaTags");
      metaTagsField.setAccessible(true);
      List<String> metaTags = (List<String>) metaTagsField.get(dest);

      metaTags.addAll(opts.metaTags());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void map(AudioEncodingOptions opts, FFmpegOutputBuilder dest) {
    setEnabled(opts.enabled(), dest::enableAudio, dest::disableAudio);
    setIfNotNull(opts.codec(), dest::setAudioCodec);
    setConditional(opts.channels(), (channels) -> channels > 0, dest::setAudioChannels);
    setConditional(opts.sampleRate(), (sampleRate) -> sampleRate > 0, dest::setAudioSampleRate);
    setIfNotNull(opts.sampleFormat(), dest::setAudioSampleFormat);
    setConditional(opts.bitRate(), (bitRate) -> bitRate > 0, dest::setAudioBitRate);
    setIfNotNull(opts.quality(), dest::setAudioQuality);
    setIfNotNull(opts.preset(), dest::setAudioPreset);
    setIfNotNull(opts.bitStreamFilter(), dest::setAudioBitStreamFilter);
    setIfNotNull(opts.filter(), dest::setAudioFilter);
  }

  public static void map(VideoEncodingOptions opts, FFmpegOutputBuilder dest) {
    setEnabled(opts.enabled(), dest::enableVideo, dest::disableVideo);
    setIfNotNull(opts.codec(), dest::setVideoCodec);
    setIfNotNull(opts.frameRate(), dest::setVideoFrameRate);
    setConditional(opts.width(), (width) -> width > 0 || width == -1, dest::setVideoWidth);
    setConditional(opts.height(), (height) -> height > 0 || height == -1, dest::setVideoHeight);
    setConditional(opts.bitRate(), (bitRate) -> bitRate > 0, dest::setVideoBitRate);
    setConditional(opts.frames(), (frames) -> frames > 0, dest::setVideoFrameRate);
    setIfNotNull(opts.filter(), dest::setVideoFilter);
    setIfNotNull(opts.preset(), dest::setVideoPreset);
    setIfNotNull(opts.videoSize(), dest::setVideoResolution);
    setIfNotNull(opts.videoMovflags(), dest::setVideoMovFlags);
    setIfNotNull(opts.videoPixelFormat(), dest::setVideoPixelFormat);
    setIfNotNull(opts.constantRateFactor(), dest::setConstantRateFactor);
    setIfNotNull(opts.quality(), dest::setVideoQuality);
    setIfNotNull(opts.bitStreamFilter(), dest::setVideoBitStreamFilter);
    setIfNotNull(opts.copyinkf(), dest::setVideoCopyInkf);
  }

  public static void map(SubtitleEncodingOptions opts, FFmpegOutputBuilder dest) {
    setEnabled(opts.enabled(), dest::enableSubtitle, dest::disableSubtitle);
    setIfNotNull(opts.codec(), dest::setSubtitleCodec);
    setIfNotNull(opts.preset(), dest::setSubtitlePreset);
  }

  public static void map(EncodingOptions opts, FFmpegOutputBuilder dest) {
    map(opts.main(), dest);

    if (opts.audio().enabled()) {
      map(opts.audio(), dest);
    }
    if (opts.video().enabled()) {
      map(opts.video(), dest);
    }
    if (opts.subtitle().enabled()) {
      map(opts.subtitle(), dest);
    }
  }

  private static <T> void setIfNotNull(@Nullable T value, Consumer<T> setter) {
    setConditional(value, Objects::nonNull, setter);
  }

  private static <T> void setConditional(
      @Nullable T value, Predicate<T> condition, Consumer<T> setter) {
    if (value != null && condition.test(value)) {
      setter.accept(value);
    }
  }

  private static void setEnabled(boolean enabled, Runnable enable, Runnable disable) {
    if (enabled) {
      enable.run();
    } else {
      disable.run();
    }
  }
}
