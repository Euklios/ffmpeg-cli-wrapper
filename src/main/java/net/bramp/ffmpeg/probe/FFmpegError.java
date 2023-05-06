package net.bramp.ffmpeg.probe;

public record FFmpegError(
        int code,
        String string
) {
}
