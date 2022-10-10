package net.bramp.ffmpeg.info;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.awt.*;

public class ColorDescriptor {
  private final String name;
  private final Color color;

  public ColorDescriptor(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  @Override
  public String toString() {
    return name + " " + color;
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
