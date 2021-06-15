package edu.fer.avsp.lab6;

import edu.fer.avsp.util.InputReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringJoiner;

public class Dgim {

  private int n;
  private int timestamp = 0;
  public LinkedList<Bucket> buckets = new LinkedList<>();

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new Dgim().solve(in, out);
  }

  public void solve(InputReader in, PrintWriter out) {
    n = in.readInt();

    while (true) {
      String line = in.readLine();
      if (line == null || line.isEmpty()) {
        break;
      }

      if (line.startsWith("q")) {
        int k = Integer.parseInt(line.split(" ")[1]);
        out.println(query(k));
        continue;
      }

      for (char bit : line.toCharArray()) {
        timestamp++;

        dropOldBuckets();

        if (bit == '1') {
          buckets.add(new Bucket(timestamp));
          mergeBuckets();
        }
      }
    }

    out.flush();
  }

  private void dropOldBuckets() {
    Iterator<Bucket> iterator = buckets.iterator();
    while (iterator.hasNext()) {
      Bucket curr = iterator.next();
      if (curr.timestamp <= timestamp - n) {
        iterator.remove();
        continue;
      }

      break;
    }
  }

  private int query(int k) {
    // start from the newest bucket
    Iterator<Bucket> iterator = buckets.descendingIterator();

    int sum = 0;
    Bucket curr;
    Bucket prev = null;
    while (iterator.hasNext()) {
      curr = iterator.next();
      if (curr.timestamp <= timestamp - k) {
        break;
      }

      sum += curr.count();
      prev = curr;
    }

    if (prev == null) {
      return 0;
    }

    // remove half of the last bucket
    return sum - (int) Math.round(prev.count() / 2.);
  }

  private void mergeBuckets() {
    Iterator<Bucket> iterator = buckets.descendingIterator();
    Bucket prev = iterator.next();
    Bucket curr;

    int duplicateCounter = 0;
    while (iterator.hasNext()) {
      curr = iterator.next();
      if (prev.exponent != curr.exponent) {
        continue;
      }

      duplicateCounter++;
      if (duplicateCounter == 2) {
        iterator.remove();
        prev.exponent++;
        duplicateCounter = 0;
        continue;
      }
      prev = curr;
    }
  }

  private static class Bucket {

    private int timestamp;
    private int exponent;

    public Bucket(int timestamp) {
      this.timestamp = timestamp;
      exponent = 0;
    }

    public int count() {
      return (int) Math.pow(2, exponent);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", "{", "}")
          .add("t=" + timestamp)
          .add("e=" + exponent)
          .toString();
    }
  }
}
