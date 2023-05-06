package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FFmpegStreamSideData(
    @JsonProperty("side_data_type") String sideDataType, String displaymatrix, int rotation) {}
