package edu.fer.avsp.util;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;

public class Pair<T extends Comparable<T>, R extends Comparable<R>>  implements Comparable<Pair<T, R>> {

  public final T first;
  public final R second;

  public Pair(T first, R second) {
    this.first = Objects.requireNonNull(first);
    this.second = Objects.requireNonNull(second);
  }

  public T getFirst() {
    return first;
  }

  public R getSecond() {
    return second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair<?, ?> pair)) {
      return false;
    }
    return first.equals(pair.first) && second.equals(pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Pair.class.getSimpleName() + "[", "]")
            .add("first=" + first)
            .add("second=" + second)
            .toString();
  }

  @Override
  public int compareTo(Pair<T, R> other) {
    return Comparator.comparing((Pair<T, R> pair) -> pair.first)
                     .thenComparing(pair -> pair.second)
                     .compare(this, other);
  }
}
