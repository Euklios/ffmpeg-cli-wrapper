package net.bramp.ffmpeg.options;

import org.apache.commons.lang3.math.Fraction;

/**
 * Encoding options for video
 *
 * @author bramp
 */
public record VideoEncodingOptions(
        boolean enabled,
        String codec,
        Fraction frameRate,
        int width,
        int height,
        long bitRate,
        Integer frames,
        String filter,
        String preset) {
}
