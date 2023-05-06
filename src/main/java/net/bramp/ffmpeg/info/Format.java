package net.bramp.ffmpeg.info;

/**
 * Information about supported Format
 *
 * @author bramp
 */
public record Format(
        String name,
        String longName,
        boolean canDemux,
        boolean canMux
) {
  @Override
  public String toString() {
    return name + " " + longName;
  }

  /**
   * @deprecated Use {@link Format#name()} instead
   */
  @Deprecated(forRemoval = true)
  public String getName() {
    return name;
  }

  /**
   * @deprecated Use {@link Format#longName()} instead
   */
  @Deprecated(forRemoval = true)
  public String getLongName() {
    return longName;
  }

  /**
   * @deprecated Use {@link Format#canDemux()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean getCanDemux() {
    return canDemux;
  }

  /**
   * @deprecated Use {@link Format#canMux()} instead
   */
  @Deprecated(forRemoval = true)
  public boolean getCanMux() {
    return canMux;
  }
}
