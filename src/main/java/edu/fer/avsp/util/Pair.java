package edu.fer.avsp.util;

import java.util.Objects;

public class Pair<T, R> {

  public final T first;
  public final R second;

  public Pair(T first, R second) {
    this.first = Objects.requireNonNull(first);
    this.second = Objects.requireNonNull(second);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair)) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return first.equals(pair.first) && second.equals(pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
