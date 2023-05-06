package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FFmpegChapter(
    int id,
    @JsonProperty("time_base") String timeBase,
    long start,
    @JsonProperty("start_time") String startTime,
    long end,
    @JsonProperty("end_time") String endTime,
    FFmpegChapterTag tags) {}
