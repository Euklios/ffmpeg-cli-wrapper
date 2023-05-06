package net.bramp.ffmpeg.info;

import com.google.common.base.Preconditions;

public class CodecFactory {
    private CodecFactory() {
        throw new AssertionError("No instances for you!");
    }

    /**
     * @param name short codec name
     * @param longName long codec name
     * @param flags is expected to be in the following format:
     *     <pre>
     * D..... = Decoding supported
     * .E.... = Encoding supported
     * ..V... = Video codec
     * ..A... = Audio codec
     * ..S... = Subtitle codec
     * ...I.. = Intra frame-only codec
     * ....L. = Lossy compression
     * .....S = Lossless compression
     * </pre>
     *
     * @return Codec
     */
    public static Codec create(String name, String longName, String flags) {
        name = Preconditions.checkNotNull(name).trim();
        longName = Preconditions.checkNotNull(longName).trim();

        Preconditions.checkNotNull(flags);
        Preconditions.checkArgument(flags.length() == 6, "Format flags is invalid '{}'", flags);

        boolean canDecode = flags.charAt(0) == 'D';
        boolean canEncode = flags.charAt(1) == 'E';

        CodecType type = switch (flags.charAt(2)) {
            case 'V' -> CodecType.VIDEO;
            case 'A' -> CodecType.AUDIO;
            case 'S' -> CodecType.SUBTITLE;
            case 'D' -> CodecType.DATA;
            default -> throw new IllegalArgumentException("Invalid codec type '" + flags.charAt(2) + "'");
        };

        // TODO There are more flags to parse

        return new Codec(name, longName, canDecode, canEncode, type);
    }
}
