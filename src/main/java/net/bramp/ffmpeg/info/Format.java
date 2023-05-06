package net.bramp.ffmpeg.info;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Information about supported Format
 *
 * @author bramp
 */
public final class Format {
  final String name;
  final String longName;

  final boolean canDemux;
  final boolean canMux;

  Format(String name, String longName, boolean canDemux, boolean canMux) {
    this.name = name;
    this.longName = longName;
    this.canDemux = canDemux;
    this.canMux = canMux;
  }

  @Override
  public String toString() {
    return name + " " + longName;
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public String getName() {
    return name;
  }

  public String getLongName() {
    return longName;
  }

  public boolean getCanDemux() {
    return canDemux;
  }

  public boolean getCanMux() {
    return canMux;
  }
}
