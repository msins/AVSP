package edu.fer.avsp.lab6;

import edu.fer.avsp.util.InputReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Dgim {

  private int N;
  private int timestamp = 0;
  public List<Bucket> buckets = new LinkedList<>();

  public static void main(String[] args) {

  }

  public void solve(InputReader in, PrintWriter out) {
    N = in.readInt();

    while (true) {
      String line = in.readLineTrimmed();
      if (line.isEmpty()) {
        break;
      }

      if (line.startsWith("q")) {
        int k = Integer.parseInt(line.split("\\s+")[1]);
        continue;
      }

      //bits
      for (int bit : line.toCharArray()) {
        timestamp++;

        // drop the last bucket if it has no overlap with window
        buckets = buckets.stream()
            .dropWhile(bucket -> timestamp - bucket.timestamp >= N)
            .collect(Collectors.toCollection(LinkedList::new));

        if (bit == '1') {
          buckets.add(new Bucket(timestamp % N));
          merge();
        }
      }

    }


  }

  private void merge() {
    ListIterator<Bucket> iterator = buckets.listIterator();
    var sameSizeCount = 0;
    Bucket curr = iterator.next();
    Bucket prev = null;
    while (iterator.hasNext()) {
      if (prev != null && prev.getExponent() == curr.getExponent()) {

      }
      if (sameSizeCount >= 3) {

        break;
      }
      sameSizeCount++;

      prev = curr;
    }
  }

  private static class Bucket {

    private int timestamp;
    private int exponent;

    public Bucket(int t) {
      timestamp = t;
      exponent = 0;
    }

    public int getCount() {
      return (int) Math.pow(2, exponent);
    }

    public void setExponent(int exponent) {
      this.exponent = exponent;
    }

    public int getExponent() {
      return exponent;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(int timestamp) {
      this.timestamp = timestamp;
    }
  }
}
