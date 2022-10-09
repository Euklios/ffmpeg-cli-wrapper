package net.bramp.ffmpeg.info;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressFBWarnings(
    value = {"UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD"},
    justification = "POJO objects where the fields are populated by gson")
public class Protocol {
  private final String name;
  private final ProtocolDirection direction;

  public Protocol(String name, ProtocolDirection direction) {
    this.name = name;
    this.direction = direction;
  }

  @Override
  public String toString() {
    return name + " (" + direction.toString() + ")";
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

  public ProtocolDirection getDirection() {
    return direction;
  }

  public enum ProtocolDirection {
    INPUT,
    OUTPUT
  }
}
