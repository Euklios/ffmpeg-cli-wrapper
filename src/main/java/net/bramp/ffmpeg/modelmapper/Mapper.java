package net.bramp.ffmpeg.modelmapper;

import net.bramp.ffmpeg.builder.AbstractFFmpegStreamBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.AudioEncodingOptions;
import net.bramp.ffmpeg.options.EncodingOptions;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
            outputBuilder.getFormat(), outputBuilder.getStartOffset(), outputBuilder.getDuration()),
        new AudioEncodingOptions(
            outputBuilder.isAudioEnabled(),
            outputBuilder.getAudioCodec(),
            outputBuilder.getAudioChannels(),
            outputBuilder.getAudioSampleRate(),
            outputBuilder.getAudioSampleFormat(),
            outputBuilder.getAudioBitRate(),
            outputBuilder.getAudioQuality()),
        new VideoEncodingOptions(
            outputBuilder.isVideoEnabled(),
            outputBuilder.getVideoCodec(),
            outputBuilder.getVideoFrameRate(),
            outputBuilder.getVideoWidth(),
            outputBuilder.getVideoHeight(),
            outputBuilder.getVideoBitRate(),
            outputBuilder.getVideoFrames(),
            outputBuilder.getVideoFilter(),
            outputBuilder.getVideoPreset()));
  }

  /** Simple wrapper object, to inject the word "audio" in the property name */
  record AudioWrapper(AudioEncodingOptions audio) {}

  /** Simple wrapper object, to inject the word "video" in the property name */
  record VideoWrapper(VideoEncodingOptions video) {}

  public static <T extends AbstractFFmpegStreamBuilder<T>> void map(
      MainEncodingOptions opts, FFmpegOutputBuilder dest) {
    setIfNotNull(opts.duration(), (duration) -> dest.setDuration(duration, TimeUnit.MILLISECONDS));
    setIfNotNull(opts.format(), dest::setFormat);
    setIfNotNull(opts.startOffset(), (startOffset) -> dest.setStartOffset(startOffset, TimeUnit.MILLISECONDS));
  }

  public static void map(
      AudioEncodingOptions opts, FFmpegOutputBuilder dest) {
    setConditional(opts.enabled(), (enabled) -> !enabled, disabled -> dest.disableAudio());
    setConditional(opts.enabled(), (enabled) -> enabled, enabled -> dest.enableAudio());
    setIfNotNull(opts.codec(), dest::setAudioCodec);
    setConditional(opts.channels(), (channels) -> channels > 0, dest::setAudioChannels);
    setConditional(opts.sampleRate(), (sampleRate) -> sampleRate > 0, dest::setAudioSampleRate);
    setIfNotNull(opts.sampleFormat(), dest::setAudioSampleFormat);
    setConditional(opts.bitRate(), (bitRate) -> bitRate > 0, dest::setAudioBitRate);
    setIfNotNull(opts.quality(), dest::setAudioQuality);
  }

  public static <T extends AbstractFFmpegStreamBuilder<T>> void map(
      VideoEncodingOptions opts, FFmpegOutputBuilder dest) {
    setConditional(opts.enabled(), (enabled) -> !enabled, disabled -> dest.disableVideo());
    setConditional(opts.enabled(), (enabled) -> enabled, enabled -> dest.enableVideo());
    setIfNotNull(opts.codec(), dest::setVideoCodec);
    setIfNotNull(opts.frameRate(), dest::setVideoFrameRate);
    setConditional(opts.width(), (width) -> width > 0 || width == -1, dest::setVideoWidth);
    setConditional(opts.height(), (height) -> height > 0 || height == -1, dest::setVideoHeight);
    setConditional(opts.bitRate(), (bitRate) -> bitRate > 0, dest::setVideoBitRate);
    setConditional(opts.frames(), (frames) -> frames > 0, dest::setVideoFrameRate);
    setIfNotNull(opts.filter(), dest::setVideoFilter);
    setIfNotNull(opts.preset(), dest::setVideoPreset);
  }

  public static void map(
      EncodingOptions opts, FFmpegOutputBuilder dest) {
    map(opts.main(), dest);

    if (opts.audio().enabled()) {
      map(opts.audio(), dest);
    }
    if (opts.video().enabled()) {
      map(opts.video(), dest);
    }
  }

  private static <T> void setIfNotNull(@Nullable T value, Consumer<T> setter) {
    setConditional(value, Objects::nonNull, setter);
  }

  private static <T> void setConditional(@Nullable T value, Predicate<T> condition, Consumer<T> setter) {
    if (value != null && condition.test(value)) {
      setter.accept(value);
    }
  }
}
