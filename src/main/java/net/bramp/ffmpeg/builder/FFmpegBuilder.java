package net.bramp.ffmpeg.builder;

import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotNull;
import static net.bramp.ffmpeg.Preconditions.checkNotEmpty;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckReturnValue;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

/**
 * Builds a ffmpeg command line
 *
 * @author bramp
 */
public class FFmpegBuilder {

  public enum Strict {
    VERY, // strictly conform to a older more strict version of the specifications or reference
    // software
    STRICT, // strictly conform to all the things in the specificiations no matter what consequences
    NORMAL, // normal
    UNOFFICIAL, // allow unofficial extensions
    EXPERIMENTAL;

    // ffmpeg command line requires these options in lower case
    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  /** Log level options: https://ffmpeg.org/ffmpeg.html#Generic-options */
  public enum Verbosity {
    QUIET,
    PANIC,
    FATAL,
    ERROR,
    WARNING,
    INFO,
    VERBOSE,
    DEBUG;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  // Global Settings
  boolean override = true;
  int pass = 0;
  String passDirectory = "";
  String passPrefix;
  Verbosity verbosity = Verbosity.ERROR;
  URI progress;
  String userAgent;

  // Input settings
  String format;
  Long startOffset; // in millis
  boolean readAtNativeFrameRate = false;
  final List<FFmpegInputBuilder> inputs = new ArrayList<>();
  final Map<String, FFmpegProbeResult> inputProbes = new TreeMap<>();

  final List<String> extraArgs = new ArrayList<>();

  // Output
  final List<FFmpegOutputBuilder> outputs = new ArrayList<>();

  // Filters
  String audioFilter;
  String videoFilter;
  String complexFilter;

  public FFmpegBuilder overrideOutputFiles(boolean override) {
    this.override = override;
    return this;
  }

  public boolean getOverrideOutputFiles() {
    return this.override;
  }

  public FFmpegBuilder setPass(int pass) {
    this.pass = pass;
    return this;
  }

  public FFmpegBuilder setPassDirectory(String directory) {
    this.passDirectory = checkNotNull(directory);
    return this;
  }

  public FFmpegBuilder setPassPrefix(String prefix) {
    this.passPrefix = checkNotNull(prefix);
    return this;
  }

  public FFmpegBuilder setVerbosity(Verbosity verbosity) {
    checkNotNull(verbosity);
    this.verbosity = verbosity;
    return this;
  }

  public FFmpegBuilder setUserAgent(String userAgent) {
    this.userAgent = checkNotNull(userAgent);
    return this;
  }

  public FFmpegBuilder readAtNativeFrameRate() {
    this.readAtNativeFrameRate = true;
    return this;
  }

  public FFmpegInputBuilder addInput(FFmpegProbeResult result) {
    checkNotNull(result);
    String filename = checkNotNull(result.format()).filename();
    inputProbes.put(filename, result);
    return addInput(filename);
  }

  public FFmpegInputBuilder addInput(String filename) {
    FFmpegInputBuilder inputBuilder = new FFmpegInputBuilder(this, filename);
    inputBuilder.setFilename(filename);

    checkNotNull(filename);
    inputs.add(inputBuilder);
    return inputBuilder;
  }

  /**
   * Adds an existing FFmpegInputBuilder. This is similar to calling the other addInput methods but
   * instead allows an existing FFmpegInputBuilder to be used, and reused.
   *
   * <pre>
   * <code>List&lt;String&gt; args = new FFmpegBuilder()
   *   .addInput(new FFmpegInputBuilder()
   *     .setFilename(&quot;input.flv&quot;)
   *     .done()
   *   )
   *   .build();</code>
   * </pre>
   *
   * @param inputBuilder FFmpegInputBuilder to add
   * @return this
   */
  public FFmpegBuilder addInput(FFmpegInputBuilder inputBuilder) {
    checkNotNull(inputBuilder);
    inputs.add(inputBuilder);

    return this;
  }

  protected void clearInputs() {
    inputs.clear();
    inputProbes.clear();
  }

  public FFmpegInputBuilder setInput(FFmpegProbeResult result) {
    clearInputs();
    return addInput(result);
  }

  public FFmpegInputBuilder setInput(String filename) {
    clearInputs();
    return addInput(filename);
  }

  public FFmpegBuilder setInput(FFmpegInputBuilder inputBuilder) {
    clearInputs();
    return addInput(inputBuilder);
  }

  public FFmpegBuilder setFormat(String format) {
    this.format = checkNotNull(format);
    return this;
  }

  public FFmpegBuilder setStartOffset(long duration, TimeUnit units) {
    checkNotNull(units);

    this.startOffset = units.toMillis(duration);

    return this;
  }

  public FFmpegBuilder addProgress(URI uri) {
    this.progress = checkNotNull(uri);
    return this;
  }

  /**
   * Sets the complex filter flag.
   *
   * @param filter
   * @return
   */
  public FFmpegBuilder setComplexFilter(String filter) {
    this.complexFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Sets the audio filter flag.
   *
   * @param filter
   * @return
   */
  public FFmpegBuilder setAudioFilter(String filter) {
    this.audioFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Sets the video filter flag.
   *
   * @param filter
   * @return
   */
  public FFmpegBuilder setVideoFilter(String filter) {
    this.videoFilter = checkNotEmpty(filter, "filter must not be empty");
    return this;
  }

  /**
   * Add additional ouput arguments (for flags which aren't currently supported).
   *
   * @param values The extra arguments.
   * @return this
   */
  public FFmpegBuilder addExtraArgs(String... values) {
    checkArgument(values.length > 0, "one or more values must be supplied");
    checkNotEmpty(values[0], "first extra arg may not be empty");

    for (String value : values) {
      extraArgs.add(checkNotNull(value));
    }
    return this;
  }

  /**
   * Adds new output file.
   *
   * @param filename output file path
   * @return A new {@link FFmpegOutputBuilder}
   */
  public FFmpegOutputBuilder addOutput(String filename) {
    FFmpegOutputBuilder output = new FFmpegOutputBuilder(this, filename);
    outputs.add(output);
    return output;
  }

  /**
   * Adds new output file.
   *
   * @param uri output file uri typically a stream
   * @return A new {@link FFmpegOutputBuilder}
   */
  public FFmpegOutputBuilder addOutput(URI uri) {
    FFmpegOutputBuilder output = new FFmpegOutputBuilder(this, uri);
    outputs.add(output);
    return output;
  }

  /**
   * Adds an existing FFmpegOutputBuilder. This is similar to calling the other addOuput methods but
   * instead allows an existing FFmpegOutputBuilder to be used, and reused.
   *
   * <pre>
   * <code>List&lt;String&gt; args = new FFmpegBuilder()
   *   .addOutput(new FFmpegOutputBuilder()
   *     .setFilename(&quot;output.flv&quot;)
   *     .setVideoCodec(&quot;flv&quot;)
   *   )
   *   .build();</code>
   * </pre>
   *
   * @param output FFmpegOutputBuilder to add
   * @return this
   */
  public FFmpegBuilder addOutput(FFmpegOutputBuilder output) {
    outputs.add(output);
    return this;
  }

  /**
   * Create new output (to stdout)
   *
   * @return A new {@link FFmpegOutputBuilder}
   */
  public FFmpegOutputBuilder addStdoutOutput() {
    return addOutput("-");
  }

  @CheckReturnValue
  public List<String> build() {
    ImmutableListBuilder<String> args = new ImmutableListBuilder<>();

    checkArgument(!inputs.isEmpty(), "At least one input must be specified");
    checkArgument(!outputs.isEmpty(), "At least one output must be specified");

    buildGlobalOptions(args);

    for (FFmpegInputBuilder input : inputs) {
      args.addAll(input.build(this, pass));
    }

    // TODO: -pass and -passlogfile are output options and shouldn't be set here
    if (pass > 0) {
      args.add("-pass", Integer.toString(pass));

      if (passPrefix != null) {
        args.add("-passlogfile", passDirectory + passPrefix);
      }
    }

    // TODO: -af is an output option and shouldn't be set here
    args.addArgIf(!Strings.isNullOrEmpty(audioFilter), "-af", audioFilter);

    // TODO: -vf is an output option and shouldn't be set here
    args.addArgIf(!Strings.isNullOrEmpty(videoFilter), "-vf", videoFilter);

    // TODO: -filter_complex is an output option and shouldn't be set here
    args.addArgIf(!Strings.isNullOrEmpty(complexFilter), "-filter_complex", complexFilter);

    for (FFmpegOutputBuilder output : this.outputs) {
      args.addAll(output.build(this, pass));
    }

    return args.build();
  }

  private void buildGlobalOptions(ImmutableListBuilder<String> args) {
    args.add(override ? "-y" : "-n");
    args.add("-v", this.verbosity.toString());

    args.addArgIf(userAgent != null, "-user_agent", userAgent);

    // TODO: This is either an input or an output option and shouldn't be set here
    args.addArgIf(startOffset != null, "-ss", () -> FFmpegUtils.toTimecode(startOffset, TimeUnit.MILLISECONDS));

    // TODO: Format is an input or output option and shouldn't be set here
    // In this case, it only acts as an input option and should be removed entirely
    args.addArgIf(format != null, "-f", format);

    // TODO: This is an input option and shouldn't be set here
    // Move to FFmpegInputBuilder
    args.addFlagIf(readAtNativeFrameRate, "-re");

    args.addArgIf(progress != null, "-progress", () -> progress.toString());

    args.addAll(extraArgs);
  }
}
