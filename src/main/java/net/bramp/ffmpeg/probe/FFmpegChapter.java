package net.bramp.ffmpeg.probe;

import com.google.gson.annotations.SerializedName;

public record FFmpegChapter(
        int id,
        @SerializedName("time_base")
        String timeBase,
        long start,
        @SerializedName("start_time")
        String startTime,
        long end,
        @SerializedName("end_time")
        String endTime,
        FFmpegChapterTag tags
) {}
