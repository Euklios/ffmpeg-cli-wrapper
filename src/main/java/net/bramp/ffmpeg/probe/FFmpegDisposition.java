package net.bramp.ffmpeg.probe;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import net.bramp.ffmpeg.gson.BitsetFieldAdapter;

/** Represents the AV_DISPOSITION_* fields */
public record FFmpegDisposition(
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean _default,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean dub,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean original,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean comment,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean lyrics,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean karaoke,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean forced,
  @SerializedName("hearing_impaired")
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean hearingImpaired,
  @SerializedName("visual_impaired")
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean visualImpaired,
  @SerializedName("clean_effects")
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean cleanEffects,
  @SerializedName("attached_pic")
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean attachedPic,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean captions,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean descriptions,
  @JsonAdapter(BitsetFieldAdapter.class)
  boolean metadata){
}
