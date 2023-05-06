package net.bramp.ffmpeg.info;

import com.google.common.base.Preconditions;

public class FormatFactory {
    private FormatFactory() {
        throw new AssertionError("No instances for you!");
    }

    /**
     * @param name short format name
     * @param longName long format name
     * @param flags is expected to be in the following format:
     *     <pre>
     * D. = Demuxing supported
     * .E = Muxing supported
     * </pre>
     */
    public static Format create(String name, String longName, String flags) {
        name = Preconditions.checkNotNull(name).trim();
        longName = Preconditions.checkNotNull(longName).trim();

        Preconditions.checkNotNull(flags);
        Preconditions.checkArgument(flags.length() == 2, "Format flags is invalid '{}'", flags);
        boolean canDemux = flags.charAt(0) == 'D';
        boolean canMux = flags.charAt(1) == 'E';

        return new Format(name, longName, canDemux, canMux);
    }
}
