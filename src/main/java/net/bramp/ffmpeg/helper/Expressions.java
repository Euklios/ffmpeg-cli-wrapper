package net.bramp.ffmpeg.helper;

public class Expressions {
    public static boolean isNullOrEmpty(String arg) {
        return arg == null || arg.isEmpty();
    }

    public static boolean isNotNullOrEmpty(String arg) {
        return !isNullOrEmpty(arg);
    }
}
