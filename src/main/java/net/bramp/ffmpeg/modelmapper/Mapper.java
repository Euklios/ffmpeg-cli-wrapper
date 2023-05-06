package net.bramp.ffmpeg.modelmapper;

import static net.bramp.ffmpeg.modelmapper.NotDefaultCondition.notDefault;

import net.bramp.ffmpeg.builder.AbstractFFmpegStreamBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.options.AudioEncodingOptions;
import net.bramp.ffmpeg.options.EncodingOptions;
import net.bramp.ffmpeg.options.MainEncodingOptions;
import net.bramp.ffmpeg.options.VideoEncodingOptions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NameTokenizers;

/**
 * Copies values from one type of object to another
 *
 * @author bramp
 */
public class Mapper {

  private Mapper() {
    throw new InstantiationError("Must not instantiate this class");
  }

  private static final ModelMapper mapper = newModelMapper();

  private static <S, D> TypeMap<S, D> createTypeMap(
      ModelMapper mapper, Class<S> sourceType, Class<D> destinationType, Configuration config) {

    return mapper
        .createTypeMap(sourceType, destinationType, config)
        // We setPropertyCondition because ModelMapper seems to ignore this in
        // the config
        .setPropertyCondition(config.getPropertyCondition());
  }

  private static ModelMapper newModelMapper() {
    final ModelMapper mapper = new ModelMapper();

    Configuration config =
        mapper
            .getConfiguration()
            .copy()
            .setFieldMatchingEnabled(true)
            .setPropertyCondition(notDefault)
            .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);

    createTypeMap(mapper, MainEncodingOptions.class, FFmpegOutputBuilder.class, config);
    createTypeMap(mapper, AudioWrapper.class, FFmpegOutputBuilder.class, config);
    createTypeMap(mapper, VideoWrapper.class, FFmpegOutputBuilder.class, config);

    return mapper;
  }

  /**
   * Simple wrapper object, to inject the word "audio" in the property name
   */
    record AudioWrapper(AudioEncodingOptions audio) {
  }

  /**
   * Simple wrapper object, to inject the word "video" in the property name
   */
    record VideoWrapper(VideoEncodingOptions video) {
  }

  public static <T extends AbstractFFmpegStreamBuilder<T>> void map(
      MainEncodingOptions opts, AbstractFFmpegStreamBuilder<T> dest) {
    mapper.map(opts, dest);
  }

  public static <T extends AbstractFFmpegStreamBuilder<T>> void map(
      AudioEncodingOptions opts, AbstractFFmpegStreamBuilder<T> dest) {
    mapper.map(new AudioWrapper(opts), dest);
  }

  public static <T extends AbstractFFmpegStreamBuilder<T>> void map(
      VideoEncodingOptions opts, AbstractFFmpegStreamBuilder<T> dest) {
    mapper.map(new VideoWrapper(opts), dest);
  }

  public static <T extends AbstractFFmpegStreamBuilder<T>> void map(
      EncodingOptions opts, AbstractFFmpegStreamBuilder<T> dest) {
    map(opts.main(), dest);

    if (opts.audio().enabled()) {
      map(opts.audio(), dest);
    }
    if (opts.video().enabled()) {
      map(opts.video(), dest);
    }
  }
}
