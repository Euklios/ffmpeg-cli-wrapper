package net.bramp.ffmpeg.builder;

import static net.bramp.ffmpeg.FFmpegUtils.toTimecode;
import static net.bramp.ffmpeg.Preconditions.*;
import static net.bramp.ffmpeg.builder.MetadataSpecifier.checkValidKey;
import static net.bramp.ffmpeg.helper.Expressions.isNotNullOrEmpty;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.options.EncodingOptions;
import org.apache.commons.lang3.math.Fraction;

/**
 * This abstract class holds flags that are both applicable to input and output streams in the
 * ffmpeg command, while flags that apply to a particular direction (input/output) are located in
 * {@link FFmpegOutputBuilder}. <br>
 * <br>
 * All possible flags can be found in the <a href="https://ffmpeg.org/ffmpeg.html#Options">official
 * ffmpeg page</a> The discrimination criteria for flag location are the specifiers for each command
 *
 * <ul>
 *   <li>AbstractFFmpegStreamBuilder
 *       <ul>
 *         <li>(input/output): <code>-t duration (input/output)</code>
 *         <li>(input/output,per-stream): <code>
 *             -codec[:stream_specifier] codec (input/output,per-stream)</code>
 *         <li>(global): <code>-filter_threads nb_threads (global)</code>
 *       </ul>
 *   <li>FFmpegInputBuilder
 *       <ul>
 *         <li>(input): <code>-muxdelay seconds (input)</code>
 *         <li>(input,per-stream): <code>-guess_layout_max channels (input,per-stream)</code>
 *       </ul>
 *   <li>FFmpegOutputBuilder
 *       <ul>
 *         <li>(output): <code>-atag fourcc/tag (output)</code>
 *         <li>(output,per-stream): <code>
 *             -bsf[:stream_specifier] bitstream_filters (output,per-stream)</code>
 *       </ul>
 * </ul>
 *
 * @param <T> A concrete class that extends from the AbstractFFmpegStreamBuilder
 */
public abstract class AbstractFFmpegStreamBuilder<T extends AbstractFFmpegStreamBuilder<T>> {
  protected final FFmpegBuilder parent;

  /** Output filename or uri. Only one may be set */
  protected String filename;

  protected URI uri;

  protected String format;

  protected Long startOffset; // in milliseconds
  protected Long duration; // in milliseconds

  protected final List<String> metaTags = new ArrayList<>();

  protected boolean audioEnabled = true;
  protected String audioCodec;
  protected int audioChannels;
  protected int audioSampleRate;
  protected String audioPreset;

  protected boolean videoEnabled = true;
  protected String videoCodec;
  protected boolean videoCopyinkf;
  protected Fraction videoFrameRate;
  protected int videoWidth;
  protected int videoHeight;
  protected String videoSize;
  protected String videoMovflags;
  protected Integer videoFrames;
  protected String videoPixelFormat;

  protected boolean subtitleEnabled = true;
  protected String subtitlePreset;
  protected String subtitleCodec;

  protected String preset;
  protected String presetFilename;
  protected final List<String> extraArgs = new ArrayList<>();

  protected Strict strict = Strict.NORMAL;

  protected long targetSize = 0; // in bytes
  protected long passPaddingBitrate = 1024; // in bits per second

  protected boolean throwWarnings = true; // TODO Either delete this, or apply it consistently

  protected AbstractFFmpegStreamBuilder() {
    this.parent = null;
  }

  protected AbstractFFmpegStreamBuilder(FFmpegBuilder parent, String filename) {
    this.parent = checkNotNull(parent);
    this.filename = checkNotEmpty(filename, "filename must not be empty");
  }

  protected AbstractFFmpegStreamBuilder(FFmpegBuilder parent, URI uri) {
    this.parent = checkNotNull(parent);
    this.uri = checkValidStream(uri);
  }

  protected abstract T getThis();

  public T disableVideo() {
    this.videoEnabled = false;
    return getThis();
  }

  public T enableVideo() {
    this.videoEnabled = true;
    return getThis();
  }

  public T disableAudio() {
    this.audioEnabled = false;
    return getThis();
  }

  public T enableAudio() {
    this.audioEnabled = true;
    return getThis();
  }

  public T disableSubtitle() {
    this.subtitleEnabled = false;
    return getThis();
  }

  public T enableSubtitle() {
    this.subtitleEnabled = true;
    return getThis();
  }

  /**
   * Sets a file to use containing presets.
   *
   * <p>Uses `-fpre`.
   *
   * @param presetFilename the preset by filename
   * @return this
   */
  public T setPresetFilename(String presetFilename) {
    this.presetFilename = checkNotEmpty(presetFilename, "file preset must not be empty");
    return getThis();
  }

  /**
   * Sets a preset by name (this only works with some codecs).
   *
   * <p>Uses `-preset`.
   *
   * @param preset the preset
   * @return this
   */
  public T setPreset(String preset) {
    this.preset = checkNotEmpty(preset, "preset must not be empty");
    return getThis();
  }

  public T setFilename(String filename) {
    this.filename = checkNotEmpty(filename, "filename must not be empty");
    return getThis();
  }

  public String getFilename() {
    return filename;
  }

  public T setUri(URI uri) {
    this.uri = checkValidStream(uri);
    return getThis();
  }

  public URI getUri() {
    return uri;
  }

  public T setFormat(String format) {
    this.format = checkNotEmpty(format, "format must not be empty");
    return getThis();
  }

  public T setVideoCodec(String codec) {
    this.enableVideo();
    this.videoCodec = checkNotEmpty(codec, "codec must not be empty");
    return getThis();
  }

  public T setVideoCopyInkf(boolean copyinkf) {
    this.enableVideo();
    this.videoCopyinkf = copyinkf;
    return getThis();
  }

  public T setVideoMovFlags(String movflags) {
    this.enableVideo();
    this.videoMovflags = checkNotEmpty(movflags, "movflags must not be empty");
    return getThis();
  }

  /**
   * Sets the video's frame rate
   *
   * @param frameRate Frames per second
   * @return this
   * @see net.bramp.ffmpeg.FFmpeg#FPS_30
   * @see net.bramp.ffmpeg.FFmpeg#FPS_29_97
   * @see net.bramp.ffmpeg.FFmpeg#FPS_24
   * @see net.bramp.ffmpeg.FFmpeg#FPS_23_976
   */
  public T setVideoFrameRate(Fraction frameRate) {
    this.enableVideo();
    this.videoFrameRate = checkNotNull(frameRate);
    return getThis();
  }

  /**
   * Set the video frame rate in terms of frames per interval. For example 24fps would be 24/1,
   * however NTSC TV at 23.976fps would be 24000 per 1001.
   *
   * @param frames The number of frames within the given seconds
   * @param per The number of seconds
   * @return this
   */
  public T setVideoFrameRate(int frames, int per) {
    return setVideoFrameRate(Fraction.getFraction(frames, per));
  }

  public T setVideoFrameRate(double frameRate) {
    return setVideoFrameRate(Fraction.getFraction(frameRate));
  }

  /**
   * Set the number of video frames to record.
   *
   * @param frames The number of frames
   * @return this
   */
  public T setFrames(int frames) {
    this.enableVideo();
    this.videoFrames = frames;
    return getThis();
  }

  protected static boolean isValidSize(int widthOrHeight) {
    return widthOrHeight > 0 || widthOrHeight == -1;
  }

  public T setVideoWidth(int width) {
    checkArgument(isValidSize(width), "Width must be -1 or greater than zero");

    this.enableVideo();
    this.videoWidth = width;
    return getThis();
  }

  public T setVideoHeight(int height) {
    checkArgument(isValidSize(height), "Height must be -1 or greater than zero");

    this.enableVideo();
    this.videoHeight = height;
    return getThis();
  }

  public T setVideoResolution(int width, int height) {
    checkArgument(
        isValidSize(width) && isValidSize(height),
        "Both width and height must be -1 or greater than zero");

    this.enableVideo();
    this.videoWidth = width;
    this.videoHeight = height;
    return getThis();
  }

  /**
   * Sets video resolution based on an abbreviation, e.g. "ntsc" for 720x480, or "vga" for 640x480
   *
   * @see <a href="https://www.ffmpeg.org/ffmpeg-utils.html#Video-size">ffmpeg video size</a>
   * @param abbreviation The abbreviation size. No validation is done, instead the value is passed
   *     as is to ffmpeg.
   * @return this
   */
  public T setVideoResolution(String abbreviation) {
    this.enableVideo();
    this.videoSize = checkNotEmpty(abbreviation, "video abbreviation must not be empty");
    return getThis();
  }

  public T setVideoPixelFormat(String format) {
    this.enableVideo();
    this.videoPixelFormat = checkNotEmpty(format, "format must not be empty");
    return getThis();
  }

  /**
   * Add metadata on output streams. Which keys are possible depends on the used codec.
   *
   * @param key Metadata key, e.g. "comment"
   * @param value Value to set for key
   * @return this
   */
  public T addMetaTag(String key, String value) {
    checkValidKey(key);
    checkNotEmpty(value, "value must not be empty");
    metaTags.add("-metadata");
    metaTags.add(key + "=" + value);
    return getThis();
  }

  /**
   * Add metadata on output streams. Which keys are possible depends on the used codec.
   *
   * <pre>{@code
   * import static net.bramp.ffmpeg.builder.MetadataSpecifier.*;
   * import static net.bramp.ffmpeg.builder.StreamSpecifier.*;
   * import static net.bramp.ffmpeg.builder.StreamSpecifierType.*;
   *
   * new FFmpegBuilder()
   *   .addMetaTag("title", "Movie Title") // Annotate whole file
   *   .addMetaTag(chapter(0), "author", "Bob") // Annotate first chapter
   *   .addMetaTag(program(0), "comment", "Awesome") // Annotate first program
   *   .addMetaTag(stream(0), "copyright", "Megacorp") // Annotate first stream
   *   .addMetaTag(stream(Video), "framerate", "24fps") // Annotate all video streams
   *   .addMetaTag(stream(Video, 0), "artist", "Joe") // Annotate first video stream
   *   .addMetaTag(stream(Audio, 0), "language", "eng") // Annotate first audio stream
   *   .addMetaTag(stream(Subtitle, 0), "language", "fre") // Annotate first subtitle stream
   *   .addMetaTag(usable(), "year", "2010") // Annotate all streams with a usable configuration
   * }</pre>
   *
   * @param spec Metadata specifier, e.g `MetadataSpec.stream(Audio, 0)`
   * @param key Metadata key, e.g. "comment"
   * @param value Value to set for key
   * @return this
   */
  public T addMetaTag(MetadataSpecifier spec, String key, String value) {
    checkValidKey(key);
    checkNotEmpty(value, "value must not be empty");
    metaTags.add("-metadata:" + spec.spec());
    metaTags.add(key + "=" + value);
    return getThis();
  }

  public T setAudioCodec(String codec) {
    this.enableAudio();
    this.audioCodec = checkNotEmpty(codec, "codec must not be empty");
    return getThis();
  }

  public T setSubtitleCodec(String codec) {
    this.enableSubtitle();
    this.subtitleCodec = checkNotEmpty(codec, "codec must not be empty");
    return getThis();
  }

  /**
   * Sets the number of audio channels
   *
   * @param channels Number of channels
   * @return this
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_MONO
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_STEREO
   */
  public T setAudioChannels(int channels) {
    checkArgument(channels > 0, "channels must be positive");
    this.enableAudio();
    this.audioChannels = channels;
    return getThis();
  }

  /**
   * Sets the Audio sample rate, for example 44_000.
   *
   * @param sampleRate Samples measured in Hz
   * @return this
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_8000
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_11025
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_12000
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_16000
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_22050
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_32000
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_44100
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_48000
   * @see net.bramp.ffmpeg.FFmpeg#AUDIO_SAMPLE_96000
   */
  public T setAudioSampleRate(int sampleRate) {
    checkArgument(sampleRate > 0, "sample rate must be positive");
    this.enableAudio();
    this.audioSampleRate = sampleRate;
    return getThis();
  }

  /**
   * Target output file size (in bytes)
   *
   * @param targetSize The target size in bytes
   * @return this
   */
  public T setTargetSize(long targetSize) {
    checkArgument(targetSize > 0, "target size must be positive");
    this.targetSize = targetSize;
    return getThis();
  }

  /**
   * Decodes but discards input until the offset.
   *
   * @param offset The offset
   * @param units The units the offset is in
   * @return this
   */
  public T setStartOffset(long offset, TimeUnit units) {
    checkNotNull(units);

    this.startOffset = units.toMillis(offset);

    return getThis();
  }

  /**
   * Stop writing the output after duration is reached.
   *
   * @param duration The duration
   * @param units The units the duration is in
   * @return this
   */
  public T setDuration(long duration, TimeUnit units) {
    checkNotNull(units);

    this.duration = units.toMillis(duration);

    return getThis();
  }

  public T setStrict(Strict strict) {
    this.strict = checkNotNull(strict);
    return getThis();
  }

  /**
   * When doing multi-pass we add a little extra padding, to ensure we reach our target
   *
   * @param bitrate bit rate
   * @return this
   */
  public T setPassPaddingBitrate(long bitrate) {
    checkArgument(bitrate > 0, "bitrate must be positive");
    this.passPaddingBitrate = bitrate;
    return getThis();
  }

  /**
   * Sets a audio preset to use.
   *
   * <p>Uses `-apre`.
   *
   * @param preset the preset
   * @return this
   */
  public T setAudioPreset(String preset) {
    this.enableAudio();
    this.audioPreset = checkNotEmpty(preset, "audio preset must not be empty");
    return getThis();
  }

  /**
   * Sets a subtitle preset to use.
   *
   * <p>Uses `-spre`.
   *
   * @param preset the preset
   * @return this
   */
  public T setSubtitlePreset(String preset) {
    this.enableSubtitle();
    this.subtitlePreset = checkNotEmpty(preset, "subtitle preset must not be empty");
    return getThis();
  }

  /**
   * Add additional output arguments (for flags which aren't currently supported).
   *
   * @param values The extra arguments
   * @return this
   */
  public T addExtraArgs(String... values) {
    checkArgument(values.length > 0, "one or more values must be supplied");
    checkNotEmpty(values[0], "first extra arg may not be empty");

    for (String value : values) {
      extraArgs.add(checkNotNull(value));
    }
    return getThis();
  }

  /**
   * Finished with this output
   *
   * @return the parent FFmpegBuilder
   */
  public FFmpegBuilder done() {
    checkState(parent != null, "Can not call done without parent being set");
    return parent;
  }

  /**
   * Returns a representation of this Builder that can be safely serialised.
   *
   * <p>NOTE: This method is horribly out of date, and its use should be rethought.
   *
   * @return A new EncodingOptions capturing this Builder's state
   */
  public abstract EncodingOptions buildOptions();

  protected List<String> build() {
    checkState(parent != null, "Can not build without parent being set");
    return build(parent);
  }

  /**
   * Builds the arguments
   *
   * @param parent The parent FFmpegBuilder
   * @return The arguments
   */
  protected List<String> build(FFmpegBuilder parent) {
    checkNotNull(parent);
    checkBuildPreconditions(parent);

    ImmutableListBuilder<String> args = new ImmutableListBuilder<>();

    args.addAll(buildInputOutputOptions(parent));
    args.addAll(buildFileNameArgument());

    return args.build();
  }

  protected List<String> buildInputOutputOptions(FFmpegBuilder parent) {
    ImmutableListBuilder<String> args = new ImmutableListBuilder<>();

    addGlobalFlags(parent, args);
    addVideoFlags(parent, args);
    addAudioFlags(args);

    if (subtitleEnabled) {
      args.addArgIf(isNotNullOrEmpty(subtitleCodec), "-scodec", subtitleCodec);
      args.addArgIf(isNotNullOrEmpty(subtitlePreset), "-spre", subtitlePreset);
    } else {
      args.add("-sn");
    }

    args.addAll(extraArgs);

    if (filename != null && uri != null) {
      throw new IllegalStateException("Only one of filename and uri can be set");
    }

    return args.build();
  }

  protected void checkBuildPreconditions(FFmpegBuilder parent) {}

  protected abstract List<String> buildFileNameArgument();

  protected void addGlobalFlags(FFmpegBuilder parent, ImmutableListBuilder<String> args) {
    args.addArgIf(strict != Strict.NORMAL, "-strict", strict.toString());
    args.addArgIf(isNotNullOrEmpty(format), "-f", format);
    args.addArgIf(isNotNullOrEmpty(preset), "-preset", preset);
    args.addArgIf(isNotNullOrEmpty(presetFilename), "-fpre", presetFilename);
    args.addArgIf(startOffset != null, "-ss", () -> toTimecode(startOffset, TimeUnit.MILLISECONDS));
    args.addArgIf(duration != null, "-t", () -> toTimecode(duration, TimeUnit.MILLISECONDS));

    args.addAll(metaTags);
  }

  protected void addAudioFlags(ImmutableListBuilder<String> args) {
    if (!audioEnabled) {
      args.add("-an");
      return;
    }

    args.addArgIf(isNotNullOrEmpty(audioCodec), "-acodec", audioCodec);
    args.addArgIf(audioChannels > 0, "-ac", String.valueOf(audioChannels));
    args.addArgIf(audioSampleRate > 0, "-ar", String.valueOf(audioSampleRate));
    args.addArgIf(isNotNullOrEmpty(audioPreset), "-apre", audioPreset);
  }

  protected void addVideoFlags(FFmpegBuilder parent, ImmutableListBuilder<String> args) {
    if (!videoEnabled) {
      args.add("-vn");
      return;
    }

    args.addArgIf(videoFrames != null, "-vframes", () -> videoFrames.toString());
    args.addArgIf(isNotNullOrEmpty(videoCodec), "-vcodec", videoCodec);
    args.addArgIf(isNotNullOrEmpty(videoPixelFormat), "-pix_fmt", videoPixelFormat);
    args.addFlagIf(videoCopyinkf, "-copyinkf");
    args.addArgIf(isNotNullOrEmpty(videoMovflags), "-movflags", videoMovflags);

    if (videoSize != null) {
      checkArgument(
          videoWidth == 0 && videoHeight == 0,
          "Can not specific width or height, as well as an abbreviatied video size");
      args.add("-s", videoSize);

    } else if (videoWidth != 0 && videoHeight != 0) {
      args.add("-s", String.format("%dx%d", videoWidth, videoHeight));
    }

    // TODO What if width is set but heigh isn't. We don't seem to do anything

    args.addArgIf(videoFrameRate != null, "-r", () -> videoFrameRate.toString());
  }

  public String getFormat() {
    return format;
  }

  public Long getStartOffset() {
    return startOffset;
  }

  public Long getDuration() {
    return duration;
  }

  public List<String> getMetaTags() {
    return List.copyOf(metaTags);
  }

  public boolean isAudioEnabled() {
    return audioEnabled;
  }

  public String getAudioCodec() {
    return audioCodec;
  }

  public int getAudioChannels() {
    return audioChannels;
  }

  public int getAudioSampleRate() {
    return audioSampleRate;
  }

  public String getAudioPreset() {
    return audioPreset;
  }

  public boolean isVideoEnabled() {
    return videoEnabled;
  }

  public String getVideoCodec() {
    return videoCodec;
  }

  public boolean isVideoCopyinkf() {
    return videoCopyinkf;
  }

  public Fraction getVideoFrameRate() {
    return videoFrameRate;
  }

  public int getVideoWidth() {
    return videoWidth;
  }

  public int getVideoHeight() {
    return videoHeight;
  }

  public String getVideoSize() {
    return videoSize;
  }

  public String getVideoMovflags() {
    return videoMovflags;
  }

  public Integer getVideoFrames() {
    return videoFrames;
  }

  public String getVideoPixelFormat() {
    return videoPixelFormat;
  }

  public boolean isSubtitleEnabled() {
    return subtitleEnabled;
  }

  public String getSubtitlePreset() {
    return subtitlePreset;
  }

  public String getSubtitleCodec() {
    return subtitleCodec;
  }

  public String getPreset() {
    return preset;
  }

  public String getPresetFilename() {
    return presetFilename;
  }

  public Strict getStrict() {
    return strict;
  }

  public long getTargetSize() {
    return targetSize;
  }

  public long getPassPaddingBitrate() {
    return passPaddingBitrate;
  }

  public boolean isThrowWarnings() {
    return throwWarnings;
  }

  public List<String> getExtraArgs() {
    return extraArgs;
  }
}
