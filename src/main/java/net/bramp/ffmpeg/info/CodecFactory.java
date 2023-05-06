package net.bramp.ffmpeg.info;

import static net.bramp.ffmpeg.Preconditions.checkArgument;
import static net.bramp.ffmpeg.Preconditions.checkNotNull;

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
        name = checkNotNull(name).trim();
        longName = checkNotNull(longName).trim();

        checkNotNull(flags);
        checkArgument(flags.length() == 6, "Format flags is invalid '%s'".formatted(flags));

        boolean canDecode = flags.charAt(0) == 'D';
        boolean canEncode = flags.charAt(1) == 'E';
        boolean intraFrameOnly = flags.charAt(3) == 'I';
        boolean lossyCompression = flags.charAt(4) == 'L';
        boolean losslessCompression = flags.charAt(5) == 'S';

        CodecType type = switch (flags.charAt(2)) {
            case 'V' -> CodecType.VIDEO;
            case 'A' -> CodecType.AUDIO;
            case 'S' -> CodecType.SUBTITLE;
            case 'D' -> CodecType.DATA;
            default -> throw new IllegalArgumentException("Invalid codec type '" + flags.charAt(2) + "'");
        };

        return new Codec(name, longName, canDecode, canEncode, intraFrameOnly, lossyCompression, losslessCompression, type);
    }
}
