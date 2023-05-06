package net.bramp.ffmpeg.builder;

import static net.bramp.ffmpeg.Preconditions.*;
import static net.bramp.ffmpeg.helper.Expressions.isNotNullOrEmpty;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.CheckReturnValue;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.modelmapper.Mapper;
import net.bramp.ffmpeg.options.*;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.lang3.SystemUtils;

/** Builds a representation of a single output/encoding setting */
public class FFmpegOutputBuilder extends AbstractFFmpegStreamBuilder<FFmpegOutputBuilder> {
  private static final String DEVNULL = SystemUtils.IS_OS_WINDOWS ? "NUL" : "/dev/null";

  static final Pattern trailingZero = Pattern.compile("\\.0*$");

  private Double constantRateFactor;

  private String audioSampleFormat;
  private long audioBitRate;
  private Double audioQuality;
  private String audioBitStreamFilter;

  private long videoBitRate;
  private Double videoQuality;
  private String videoPreset;
  private String videoBitStreamFilter;

  // Filters
  private String audioFilter;
  private String videoFilter;
  private String complexFilter;

  // Multi-pass encoding
  private int pass = 0;
  private String passDirectory = "";
  private String passPrefix;

  public FFmpegOutputBuilder() {
    super();
  }

  protected FFmpegOutputBuilder(FFmpegBuilder parent, String filename) {
    super(parent, filename);
  }

  protected FFmpegOutputBuilder(FFmpegBuilder parent, URI uri) {
    super(parent, uri);
  }

  public FFmpegOutputBuilder useOptions(EncodingOptions opts) {
    Mapper.map(opts, this);
    return this;
  }

  public FFmpegOutputBuilder useOptions(MainEncodingOptions opts) {
    Mapper.map(opts, this);
    return this;
  }

  public FFmpegOutputBuilder useOptions(AudioEncodingOptions opts) {
    Mapper.map(opts, this);
    return this;
  }

  public FFmpegOutputBuilder useOptions(VideoEncodingOptions opts) {
    Mapper.map(opts, this);
    return this;
  }

  public FFmpegOutputBuilder useOptions(SubtitleEncodingOptions opts) {
    Mapper.map(opts, this);
    return this;
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
   * Sets the complex filter flag.
   *
   * @param filter The complex filter definition
   * @return this
   */
  public FFmpegOutputBuilder setComplexFilter(String filter) {
    this.complexFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  FFmpegOutputBuilder setPass(int pass) {
    this.pass = pass;
    return this;
  }

  FFmpegOutputBuilder setPassDirectory(String directory) {
    this.passDirectory = directory;
    return this;
  }

  FFmpegOutputBuilder setPassPrefix(String prefix) {
    this.passPrefix = prefix;
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
    return Mapper.toOptions(this);
  }

  @CheckReturnValue
  @Override
  protected List<String> build() {
    checkState(parent != null, "Can not build without parent being set");
    return build(parent);
  }

  @Override
  protected void checkBuildPreconditions(FFmpegBuilder parent) {
    if (pass > 0) {
      // TODO Write a test for this:
      checkArgument(format != null, "Format must be specified when using two-pass");
    }
  }

  /**
   * Builds the arguments
   *
   * @param parent The parent FFmpegBuilder
   * @return The arguments
   */
  @CheckReturnValue
  @Override
  protected List<String> build(FFmpegBuilder parent) {
    if (pass > 0) {
      checkArgument(
          targetSize != 0 || videoBitRate != 0,
          "Target size, or video bitrate must be specified when using two-pass");
    }
    if (targetSize > 0) {
      checkState(parent.getInputs().size() == 1, "Target size does not support multiple inputs");

      checkArgument(
          constantRateFactor == null, "Target size can not be used with constantRateFactor");

      FFmpegInputBuilder firstInput = parent.getInputs().iterator().next();
      FFmpegProbeResult input = parent.getInputProbes().get(firstInput.getFilename());

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

    ImmutableListBuilder<String> args = new ImmutableListBuilder<>();

    if (pass > 0) {
      args.add("-pass", Integer.toString(pass));

      if (passPrefix != null) {
        args.add("-passlogfile", passDirectory + passPrefix);
      }
    }

    args.addAll(super.build(parent));

    return args.build();
  }

  @Override
  protected List<String> buildFileNameArgument() {
    if (pass == 1) {
      return List.of(DEVNULL);
    } else if (filename != null) {
      return List.of(filename);
    } else if (uri != null) {
      return List.of(uri.toString());
    } else {
      assert (false);
      return List.of();
    }
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
  protected void addGlobalFlags(FFmpegBuilder parent, ImmutableListBuilder<String> args) {
    super.addGlobalFlags(parent, args);

    args.addArgIf(isNotNullOrEmpty(complexFilter), "-filter_complex", complexFilter);
    args.addArgIf(
        constantRateFactor != null, "-crf", () -> formatDecimalInteger(constantRateFactor));
  }

  @Override
  protected void addVideoFlags(FFmpegBuilder parent, ImmutableListBuilder<String> args) {
    if (!videoEnabled) {
      args.add("-vn");
      return;
    }

    super.addVideoFlags(parent, args);

    if (videoBitRate > 0 && videoQuality != null) {
      // I'm not sure, but it seems videoQuality overrides videoBitRate, so don't allow both
      throw new IllegalStateException("Only one of videoBitRate and videoQuality can be set");
    }

    args.addArgIf(videoBitRate > 0, "-b:v", String.valueOf(videoBitRate));
    args.addArgIf(videoQuality != null, "-qscale:v", () -> formatDecimalInteger(videoQuality));
    args.addArgIf(isNotNullOrEmpty(videoPreset), "-vpre", videoPreset);

    if (isNotNullOrEmpty(videoFilter)) {
      checkState(
          parent.getInputs().size() == 1,
          "Video filter only works with one input, instead use setComplexVideoFilter(..)");
      args.add("-vf", videoFilter);
    }

    args.addArgIf(isNotNullOrEmpty(videoBitStreamFilter), "-bsf:v", videoBitStreamFilter);
  }

  @Override
  protected void addAudioFlags(ImmutableListBuilder<String> args) {
    if (!audioEnabled || pass == 1) {
      args.add("-an");
      return;
    }

    super.addAudioFlags(args);

    args.addArgIf(isNotNullOrEmpty(audioSampleFormat), "-sample_fmt", audioSampleFormat);

    if (audioBitRate > 0 && audioQuality != null && throwWarnings) {
      // I'm not sure, but it seems audioQuality overrides audioBitRate, so don't allow both
      throw new IllegalStateException("Only one of audioBitRate and audioQuality can be set");
    }

    args.addArgIf(audioBitRate > 0, "-b:a", String.valueOf(audioBitRate));
    args.addArgIf(audioQuality != null, "-qscale:a", () -> formatDecimalInteger(audioQuality));
    args.addArgIf(isNotNullOrEmpty(audioBitStreamFilter), "-bsf:a", audioBitStreamFilter);
    args.addArgIf(isNotNullOrEmpty(audioFilter), "-af", audioFilter);
  }

  @CheckReturnValue
  @Override
  protected FFmpegOutputBuilder getThis() {
    return this;
  }

  public Double getConstantRateFactor() {
    return constantRateFactor;
  }

  public String getAudioSampleFormat() {
    return audioSampleFormat;
  }

  public long getAudioBitRate() {
    return audioBitRate;
  }

  public Double getAudioQuality() {
    return audioQuality;
  }

  public String getAudioBitStreamFilter() {
    return audioBitStreamFilter;
  }

  public long getVideoBitRate() {
    return videoBitRate;
  }

  public Double getVideoQuality() {
    return videoQuality;
  }

  public String getVideoPreset() {
    return videoPreset;
  }

  public String getVideoBitStreamFilter() {
    return videoBitStreamFilter;
  }

  public String getAudioFilter() {
    return audioFilter;
  }

  public String getVideoFilter() {
    return videoFilter;
  }

  public String getComplexFilter() {
    return complexFilter;
  }

  public int getPass() {
    return pass;
  }

  public String getPassDirectory() {
    return passDirectory;
  }

  public String getPassPrefix() {
    return passPrefix;
  }
}
