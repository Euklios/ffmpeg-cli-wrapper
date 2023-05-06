package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.JsonAdapter;
import java.util.Map;
import net.bramp.ffmpeg.gson.ImmutableMapAdapter;

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
    @JsonAdapter(value = ImmutableMapAdapter.class, nullSafe = false) Map<String, String> tags) {}
