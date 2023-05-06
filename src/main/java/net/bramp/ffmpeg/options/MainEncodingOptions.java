package net.bramp.ffmpeg.options;

import java.util.List;
import net.bramp.ffmpeg.builder.Strict;

/**
 * @author bramp
 */
public record MainEncodingOptions(
    String format,
    Long startOffset,
    Long duration,
    String complexFilter,
    List<String> metaTags,
    String preset,
    String presetFilename,
    List<String> extraArgs,
    Strict strict,
    long targetSize) {}
