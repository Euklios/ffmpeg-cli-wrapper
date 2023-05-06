package net.bramp.ffmpeg.probe;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the AV_DISPOSITION_* fields */
public record FFmpegDisposition(
    @JsonProperty("default") boolean _default,
    boolean dub,
    boolean original,
    boolean comment,
    boolean lyrics,
    boolean karaoke,
    boolean forced,
    @JsonProperty("hearing_impaired") boolean hearingImpaired,
    @JsonProperty("visual_impaired") boolean visualImpaired,
    @JsonProperty("clean_effects") boolean cleanEffects,
    @JsonProperty("attached_pic") boolean attachedPic,
    boolean captions,
    boolean descriptions,
    boolean metadata) {}
