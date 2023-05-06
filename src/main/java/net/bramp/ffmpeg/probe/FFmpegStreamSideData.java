package net.bramp.ffmpeg.probe;

import com.google.gson.annotations.SerializedName;

public record FFmpegStreamSideData(
    @SerializedName("side_data_type") String sideDataType, String displaymatrix, int rotation) {}
