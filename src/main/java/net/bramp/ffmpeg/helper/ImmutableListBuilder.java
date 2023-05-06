package net.bramp.ffmpeg.helper;

import java.util.*;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public class ImmutableListBuilder<T> {
  private final ArrayList<T> list;

  public ImmutableListBuilder() {
    this.list = new ArrayList<>();
  }

  public ImmutableListBuilder<T> add(T t) {
    list.add(t);
    return this;
  }

  public ImmutableListBuilder<T> addAll(@Nonnull Collection<? extends T> c) {
    list.addAll(c);
    return this;
  }

  public List<T> build() {
    return List.copyOf(this.list);
  }

  public ImmutableListBuilder<T> addFlagIf(boolean condition, T tFlag) {
    if (condition) {
      this.add(tFlag);
    }

    return this;
  }

  public ImmutableListBuilder<T> addArgIf(boolean condition, T argKey, Supplier<T> tValueSupplier) {
    if (condition) {
      this.add(argKey, tValueSupplier.get());
    }

    return this;
  }

  public ImmutableListBuilder<T> addArgIf(boolean condition, T argKey, T tValue) {
    if (condition) {
      this.add(argKey, tValue);
    }

    return this;
  }

  public ImmutableListBuilder<T> add(T t1, T t2) {
    this.list.add(t1);
    this.list.add(t2);

    return this;
  }

  @SafeVarargs
  public final ImmutableListBuilder<T> add(T... ts) {
    this.list.addAll(Arrays.stream(ts).toList());

    return this;
  }
}
