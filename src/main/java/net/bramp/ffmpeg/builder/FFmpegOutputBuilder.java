package net.bramp.ffmpeg.builder;

import static com.google.common.base.Preconditions.*;
import static net.bramp.ffmpeg.Preconditions.checkNotEmpty;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.CheckReturnValue;
import net.bramp.ffmpeg.options.AudioEncodingOptions;
import net.bramp.ffmpeg.options.EncodingOptions;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

/** Builds a representation of a single output/encoding setting */
public class FFmpegOutputBuilder extends AbstractFFmpegStreamBuilder<FFmpegOutputBuilder> {

  static final Pattern trailingZero = Pattern.compile("\\.0*$");

  public Double constantRateFactor;

  public String audioSampleFormat;
  public long audioBitRate;
  public Double audioQuality;
  public String audioBitStreamFilter;
  public String audioFilter;

  public long videoBitRate;
  public Double videoQuality;
  public String videoPreset;
  public String videoFilter;
  public String videoBitStreamFilter;

  public FFmpegOutputBuilder() {
    super();
  }

  protected FFmpegOutputBuilder(FFmpegBuilder parent, String filename) {
    super(parent, filename);
  }

  protected FFmpegOutputBuilder(FFmpegBuilder parent, URI uri) {
    super(parent, uri);
  }

  public FFmpegOutputBuilder setConstantRateFactor(double factor) {
    checkArgument(factor >= 0, "constant rate factor must be greater or equal to zero");
    this.constantRateFactor = factor;
    return this;
  }

  public FFmpegOutputBuilder setVideoBitRate(long bitRate) {
    checkArgument(bitRate > 0, "bit rate must be positive");
    this.videoEnabled = true;
    this.videoBitRate = bitRate;
    return this;
  }

  public FFmpegOutputBuilder setVideoQuality(double quality) {
    checkArgument(quality > 0, "quality must be positive");
    this.videoEnabled = true;
    this.videoQuality = quality;
    return this;
  }

  public FFmpegOutputBuilder setVideoBitStreamFilter(String filter) {
    this.videoBitStreamFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Sets a video preset to use.
   *
   * <p>Uses `-vpre`.
   *
   * @param preset the preset
   * @return this
   */
  public FFmpegOutputBuilder setVideoPreset(String preset) {
    this.videoEnabled = true;
    this.videoPreset = checkNotEmpty(preset, "video preset must not be empty");
    return this;
  }

  /**
   * Sets Video Filter
   *
   * <p>TODO Build a fluent Filter builder
   *
   * @param filter The video filter.
   * @return this
   */
  public FFmpegOutputBuilder setVideoFilter(String filter) {
    this.videoEnabled = true;
    this.videoFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Sets the audio bit depth.
   *
   * @param bitDepth The sample format, one of the net.bramp.ffmpeg.FFmpeg#AUDIO_DEPTH_* constants.
   * @return this
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_DEPTH_U8
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_DEPTH_S16
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_DEPTH_S32
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_DEPTH_FLT
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_DEPTH_DBL
   * @deprecated use {@link #setAudioSampleFormat} instead.
   */
  @Deprecated
  public FFmpegOutputBuilder setAudioBitDepth(String bitDepth) {
    return setAudioSampleFormat(bitDepth);
  }

  /**
   * Sets the audio sample format.
   *
   * @param sampleFormat The sample format, one of the net.bramp.ffmpeg.FFmpeg#AUDIO_FORMAT_*
   *     constants.
   * @return this
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_FORMAT_U8
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_FORMAT_S16
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_FORMAT_S32
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_FORMAT_FLT
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_FORMAT_DBL
   */
  public FFmpegOutputBuilder setAudioSampleFormat(String sampleFormat) {
    this.audioEnabled = true;
    this.audioSampleFormat = checkNotEmpty(sampleFormat, "sample format must not be empty");
    return this;
  }

  /**
   * Sets the Audio bit rate
   *
   * @param bitRate Audio bitrate in bits per second.
   * @return this
   */
  public FFmpegOutputBuilder setAudioBitRate(long bitRate) {
    checkArgument(bitRate > 0, "bit rate must be positive");
    this.audioEnabled = true;
    this.audioBitRate = bitRate;
    return this;
  }

  public FFmpegOutputBuilder setAudioQuality(double quality) {
    checkArgument(quality > 0, "quality must be positive");
    this.audioEnabled = true;
    this.audioQuality = quality;
    return this;
  }

  public FFmpegOutputBuilder setAudioBitStreamFilter(String filter) {
    this.audioEnabled = true;
    this.audioBitStreamFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Sets Audio Filter
   *
   * <p>TODO Build a fluent Filter builder
   *
   * @param filter The audio filter.
   * @return this
   */
  public FFmpegOutputBuilder setAudioFilter(String filter) {
    this.audioEnabled = true;
    this.audioFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Returns a representation of this Builder that can be safely serialised.
   *
   * <p>NOTE: This method is horribly out of date, and its use should be rethought.
   *
   * @return A new EncodingOptions capturing this Builder's state
   */
  @CheckReturnValue
  @Override
  public EncodingOptions buildOptions() {
    // TODO When/if modelmapper supports @ConstructorProperties, we map this
    // object, instead of doing new XXX(...)
    // https://github.com/jhalterman/modelmapper/issues/44
    return new EncodingOptions(
        new MainEncodingOptions(format, startOffset, duration),
        new AudioEncodingOptions(
                audioEnabled,
                audioCodec,
                audioChannels,
                audioSampleRate,
                audioSampleFormat,
                audioBitRate,
                audioQuality),
        new VideoEncodingOptions(
                videoEnabled,
                videoCodec,
                videoFrameRate,
                videoWidth,
                videoHeight,
                videoBitRate,
                videoFrames,
                videoFilter,
                videoPreset));
  }

  @CheckReturnValue
  @Override
  protected List<String> build(int pass) {
    Preconditions.checkState(parent != null, "Can not build without parent being set");
    return build(parent, pass);
  }

  @Override
  protected void checkBuildPreconditions(FFmpegBuilder parent, int pass) {
    if (pass > 0) {
      // TODO Write a test for this:
      checkArgument(format != null, "Format must be specified when using two-pass");
    }
  }

  /**
   * Builds the arguments
   *
   * @param parent The parent FFmpegBuilder
   * @param pass The particular pass. For one-pass this value will be zero, for multi-pass, it will
   *     be 1 for the first pass, 2 for the second, and so on.
   * @return The arguments
   */
  @CheckReturnValue
  @Override
  protected List<String> build(FFmpegBuilder parent, int pass) {
    if (pass > 0) {
      checkArgument(
          targetSize != 0 || videoBitRate != 0,
          "Target size, or video bitrate must be specified when using two-pass");
    }
    if (targetSize > 0) {
      checkState(parent.inputs.size() == 1, "Target size does not support multiple inputs");

      checkArgument(
          constantRateFactor == null, "Target size can not be used with constantRateFactor");

      FFmpegInputBuilder firstInput = parent.inputs.iterator().next();
      FFmpegProbeResult input = parent.inputProbes.get(firstInput.getFilename());

      checkState(input != null, "Target size must be used with setInput(FFmpegProbeResult)");

      // TODO factor in start time and/or number of frames

      double durationInSeconds = input.format().duration();
      long totalBitRate =
          (long) Math.floor((targetSize * 8) / durationInSeconds) - passPaddingBitrate;

      // TODO Calculate audioBitRate

      if (videoEnabled && videoBitRate == 0) {
        // Video (and possibly audio)
        videoBitRate = totalBitRate - (audioEnabled ? audioBitRate : 0);
      } else if (audioEnabled && audioBitRate == 0) {
        // Just Audio
        audioBitRate = totalBitRate;
      }
    }

    return super.build(parent, pass);
  }

  /**
   * Returns a double formatted as a string. If the double is an integer, then trailing zeros are
   * striped.
   *
   * @param d the double to format.
   * @return The formatted double.
   */
  protected static String formatDecimalInteger(double d) {
    return trailingZero.matcher(String.valueOf(d)).replaceAll("");
  }

  @Override
  protected void addGlobalFlags(FFmpegBuilder parent, ImmutableList.Builder<String> args) {
    super.addGlobalFlags(parent, args);

    if (constantRateFactor != null) {
      args.add("-crf", formatDecimalInteger(constantRateFactor));
    }
  }

  @Override
  protected void addVideoFlags(FFmpegBuilder parent, ImmutableList.Builder<String> args) {
    super.addVideoFlags(parent, args);

    if (videoBitRate > 0 && videoQuality != null) {
      // I'm not sure, but it seems videoQuality overrides videoBitRate, so don't allow both
      throw new IllegalStateException("Only one of videoBitRate and videoQuality can be set");
    }

    if (videoBitRate > 0) {
      args.add("-b:v", String.valueOf(videoBitRate));
    }

    if (videoQuality != null) {
      args.add("-qscale:v", formatDecimalInteger(videoQuality));
    }

    if (!Strings.isNullOrEmpty(videoPreset)) {
      args.add("-vpre", videoPreset);
    }

    if (!Strings.isNullOrEmpty(videoFilter)) {
      checkState(
          parent.inputs.size() == 1,
          "Video filter only works with one input, instead use setComplexVideoFilter(..)");
      args.add("-vf", videoFilter);
    }

    if (!Strings.isNullOrEmpty(videoBitStreamFilter)) {
      args.add("-bsf:v", videoBitStreamFilter);
    }
  }

  @Override
  protected void addAudioFlags(ImmutableList.Builder<String> args) {
    super.addAudioFlags(args);

    if (!Strings.isNullOrEmpty(audioSampleFormat)) {
      args.add("-sample_fmt", audioSampleFormat);
    }

    if (audioBitRate > 0 && audioQuality != null && throwWarnings) {
      // I'm not sure, but it seems audioQuality overrides audioBitRate, so don't allow both
      throw new IllegalStateException("Only one of audioBitRate and audioQuality can be set");
    }

    if (audioBitRate > 0) {
      args.add("-b:a", String.valueOf(audioBitRate));
    }

    if (audioQuality != null) {
      args.add("-qscale:a", formatDecimalInteger(audioQuality));
    }

    if (!Strings.isNullOrEmpty(audioBitStreamFilter)) {
      args.add("-bsf:a", audioBitStreamFilter);
    }

    if (!Strings.isNullOrEmpty(audioFilter)) {
      args.add("-af", audioFilter);
    }
  }

  @CheckReturnValue
  @Override
  protected FFmpegOutputBuilder getThis() {
    return this;
  }
}
