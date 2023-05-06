package net.bramp.ffmpeg.probe;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import net.bramp.ffmpeg.gson.ImmutableListAdapter;
import net.bramp.ffmpeg.gson.ImmutableMapAdapter;
import org.apache.commons.lang3.math.Fraction;

public record FFmpegStream(
        int index,
        @SerializedName("codec_name")
        String codecName,
        @SerializedName("codec_long_name")
        String codecLongName,
        String profile,
        @SerializedName("codec_type")
        FFmpegStreamCodecType codecType,
        @SerializedName("codec_time_base")
        Fraction codecTimeBase,

        @SerializedName("codec_tag_string")
        String codecTagString,
        @SerializedName("codec_tag")
        String codecTag,

        int width,
        int height,

        @SerializedName("has_b_frames")
        int hasBFrames,

        @SerializedName("sample_aspect_ratio")
        String sampleAspectRatio, // TODO Change to a Ratio/Fraction object
        @SerializedName("display_aspect_ratio")
        String displayAspectRatio,

        @SerializedName("pix_fmt")
        String pixFmt,
        int level,
        @SerializedName("chroma_location")
        String chromaLocation,
        int refs,
        @SerializedName("is_avc")
        String isAvc,
        @SerializedName("nal_length_size")
        String nalLengthSize,
        @SerializedName("r_frame_rate")
        Fraction rFrameRate,
        @SerializedName("avg_frame_rate")
        Fraction avgFrameRate,
        @SerializedName("time_base")
        Fraction timeBase,

        @SerializedName("start_pts")
        long startPts,
        @SerializedName("start_time")
        double startTime,

        @SerializedName("duration_ts")
        long durationTs,
        double duration,

        @SerializedName("bit_rate")
        long bitRate,
        @SerializedName("max_bit_rate")
        long maxBitRate,
        @SerializedName("bits_per_raw_sample")
        int bitsPerRawSample,
        @SerializedName("bits_per_sample")
        int bitsPerSample,

        @SerializedName("nb_frames")
        long nbFrames,

        @SerializedName("sample_fmt")
        String sampleFmt,
        @SerializedName("sample_rate")
        int sampleRate,
        int channels,
        @SerializedName("channel_layout")
        String channelLayout,

        FFmpegDisposition disposition,

        @JsonAdapter(value = ImmutableMapAdapter.class, nullSafe = false)
        Map<String, String> tags,

        @SerializedName("side_data_list")
        @JsonAdapter(value = ImmutableListAdapter.class, nullSafe = false)
        List<FFmpegStreamSideData> sideData) {
}
