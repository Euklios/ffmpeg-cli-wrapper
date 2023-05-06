package net.bramp.ffmpeg.probe;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
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
    @SerializedName("nb_streams") int nbStreams,
    @SerializedName("nb_programs") int nbPrograms,
    @SerializedName("format_name") String formatName,
    @SerializedName("format_long_name") String formatLongName,
    @SerializedName("start_time") double startTime,
    // TODO Change this to java.time.Duration
    double duration,
    long size,
    @SerializedName("bit_rate") long bitRate,
    @SerializedName("probe_score") int probeScore,
    @JsonAdapter(value = ImmutableMapAdapter.class, nullSafe = false) Map<String, String> tags) {}
