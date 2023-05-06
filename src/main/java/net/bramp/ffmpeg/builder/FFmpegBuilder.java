package net.bramp.ffmpeg.builder;

import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotEmpty;
import static net.bramp.ffmpeg.Preconditions.checkNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.CheckReturnValue;
import net.bramp.ffmpeg.helper.ImmutableListBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

/**
 * Builds a ffmpeg command line
 *
 * @author bramp
 */
public class FFmpegBuilder {
  // Global Settings
  boolean override = true;
  Verbosity verbosity = Verbosity.ERROR;
  URI progress;
  String userAgent;

  // Pass configuration is technically not global, but given the current architecture of TwoPassFFmpegJob, the easiest approach is to simply forward the configuration to the FFmpegOutputStreams whenever build is called
  int pass = 0;
  String passDirectory = "";
  String passPrefix;

  final List<FFmpegInputBuilder> inputs = new ArrayList<>();
  final Map<String, FFmpegProbeResult> inputProbes = new TreeMap<>();


  final List<String> extraArgs = new ArrayList<>();

  // Output
  final List<FFmpegOutputBuilder> outputs = new ArrayList<>();

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

  public FFmpegBuilder addProgress(URI uri) {
    this.progress = checkNotNull(uri);
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
    ImmutableListBuilder<String> globalArgs = new ImmutableListBuilder<>();
    ImmutableListBuilder<List<String>> inputArguments = new ImmutableListBuilder<>();
    ImmutableListBuilder<List<String>> outputArguments = new ImmutableListBuilder<>();

    checkArgument(!inputs.isEmpty(), "At least one input must be specified");
    checkArgument(!outputs.isEmpty(), "At least one output must be specified");

    buildGlobalOptions(globalArgs);

    for (FFmpegInputBuilder input : inputs) {
      inputArguments.add(input.build(this));
    }

    for (FFmpegOutputBuilder output : this.outputs) {
      output.setPass(this.pass);
      output.setPassPrefix(this.passPrefix);
      output.setPassDirectory(this.passDirectory);
      outputArguments.add(output.build(this));
    }

    return assembleArguments(globalArgs.build(), inputArguments.build(), outputArguments.build());
  }

  protected List<String> assembleArguments(List<String> globalArgs, List<List<String>> inputArguments, List<List<String>> outputArguments) {
    ImmutableListBuilder<String> args = new ImmutableListBuilder<>();

    args.addAll(globalArgs);
    inputArguments.forEach(args::addAll);
    outputArguments.forEach(args::addAll);

    return args.build();
  }

  private void buildGlobalOptions(ImmutableListBuilder<String> args) {
    args.add(override ? "-y" : "-n");
    args.add("-v", this.verbosity.toString());

    args.addArgIf(userAgent != null, "-user_agent", userAgent);
    args.addArgIf(progress != null, "-progress", () -> progress.toString());

    args.addAll(extraArgs);
  }
}
