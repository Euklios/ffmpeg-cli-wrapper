package net.bramp.ffmpeg.options;

/**
 * Encoding options for audio
 *
 * @author bramp
 */
public record AudioEncodingOptions(
    boolean enabled,
    String codec,
    int channels,
    int sampleRate,
    String sampleFormat,
    long bitRate,
    Double quality) {}
