package net.bramp.ffmpeg;

import net.bramp.ffmpeg.probe.FFmpegError;

import java.io.IOException;

public class FFmpegException extends IOException {

    private static final long serialVersionUID = 3048288225568984942L;
    private final FFmpegError error;

    public FFmpegException(String message, FFmpegError error) {
        super(message);
        this.error = error;
    }

    public FFmpegError getError() {
        return error;
    }

    @Override
    public String getMessage() {
        if (error != null) {
            return super.getMessage() + "(" + error + ")";
        } else {
            return super.getMessage();
        }
    }
}
