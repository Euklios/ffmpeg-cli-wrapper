package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * @param filename
 * @param nbStreams
 * @param nbPrograms
 * @param formatName
 * @param formatLongName
 * @param startTime Duration in seconds since start
 * @param duration
 * @param size File size in bytes
 * @param bitRate Bitrate
 * @param probeScore
 * @param tags
 */
public record FFmpegFormat(
    String filename,
    @JsonProperty("nb_streams") int nbStreams,
    @JsonProperty("nb_programs") int nbPrograms,
    @JsonProperty("format_name") String formatName,
    @JsonProperty("format_long_name") String formatLongName,
    @JsonProperty("start_time") double startTime,
    // TODO Change this to java.time.Duration
    double duration,
    long size,
    @JsonProperty("bit_rate") long bitRate,
    @JsonProperty("probe_score") int probeScore,
    Map<String, String> tags) {}
