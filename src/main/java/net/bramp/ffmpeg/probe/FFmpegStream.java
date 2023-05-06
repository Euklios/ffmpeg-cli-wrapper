package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.math.Fraction;

public record FFmpegStream(
    int index,
    @JsonProperty("codec_name") String codecName,
    @JsonProperty("codec_long_name") String codecLongName,
    String profile,
    @JsonProperty("codec_type") FFmpegStreamCodecType codecType,
    @JsonProperty("codec_time_base") Fraction codecTimeBase,
    @JsonProperty("codec_tag_string") String codecTagString,
    @JsonProperty("codec_tag") String codecTag,
    int width,
    int height,
    @JsonProperty("has_b_frames") int hasBFrames,
    @JsonProperty("sample_aspect_ratio")
        String sampleAspectRatio, // TODO Change to a Ratio/Fraction object
    @JsonProperty("display_aspect_ratio") String displayAspectRatio,
    @JsonProperty("pix_fmt") String pixFmt,
    int level,
    @JsonProperty("chroma_location") String chromaLocation,
    int refs,
    @JsonProperty("is_avc") String isAvc,
    @JsonProperty("nal_length_size") String nalLengthSize,
    @JsonProperty("r_frame_rate") Fraction rFrameRate,
    @JsonProperty("avg_frame_rate") Fraction avgFrameRate,
    @JsonProperty("time_base") Fraction timeBase,
    @JsonProperty("start_pts") long startPts,
    @JsonProperty("start_time") double startTime,
    @JsonProperty("duration_ts") long durationTs,
    double duration,
    @JsonProperty("bit_rate") long bitRate,
    @JsonProperty("max_bit_rate") long maxBitRate,
    @JsonProperty("bits_per_raw_sample") int bitsPerRawSample,
    @JsonProperty("bits_per_sample") int bitsPerSample,
    @JsonProperty("nb_frames") long nbFrames,
    @JsonProperty("sample_fmt") String sampleFmt,
    @JsonProperty("sample_rate") int sampleRate,
    int channels,
    @JsonProperty("channel_layout") String channelLayout,
    FFmpegDisposition disposition,
    Map<String, String> tags,
    @JsonProperty("side_data_list") List<FFmpegStreamSideData> sideData) {}
