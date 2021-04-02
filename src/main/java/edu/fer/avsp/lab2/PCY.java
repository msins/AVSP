package edu.fer.avsp.lab2;

import edu.fer.avsp.util.InputReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Park Chen Yu
 */
public class PCY {

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new PCY().solve(in, out);
  }

  void solve(InputReader in, PrintWriter out) {
    int basketCount = in.readInt();
    double s = in.readDouble();
    int bucketCount = in.readInt();

    var itemCounter = new HashMap<Integer, Integer>();
    var baskets = new ArrayList<Integer[]>();

    // 1st pass
    for (int i = 0; i < basketCount; i++) {
      var basket = Arrays.stream(in.readLineTrimmed().split(" "))
          .map(Integer::valueOf)
          .toArray(Integer[]::new);

      baskets.add(basket);

      for (var item : basket) {
        itemCounter.merge(item, 1, Integer::sum);
      }
    }

    int threshold = (int) Math.floor(basketCount * s);
    var frequentItemsCount = itemCounter.values().stream().filter(count -> count > threshold).count();
    var A = (int) (0.5 * frequentItemsCount * (frequentItemsCount - 1));
    int uniqueItemsCount = itemCounter.size();

    var itemPairCounter = new HashMap<Integer, Integer>();

    //2nd pass
    for (var basket : baskets) {
      for (int i = 0; i < basket.length; i++) {
        for (int j = i + 1; j < basket.length; j++) {
          var item1 = basket[i];
          var item2 = basket[j];
          //if both items pass the threshold increment their pairs counter
          if (itemCounter.get(item1) >= threshold && itemCounter.get(item2) >= threshold) {
            var hash = (item1 * uniqueItemsCount + item2) % bucketCount;
            itemPairCounter.merge(hash, 1, Integer::sum);
          }
        }
      }
    }

    var pairs = new HashMap<Integer, Integer>();

    // 3rd pass
    for (var basket : baskets) {
      for (int i = 0; i < basket.length; i++) {
        for (int j = i + 1; j < basket.length; j++) {
          var item1 = basket[i];
          var item2 = basket[j];
          if (itemCounter.get(item1) >= threshold && itemCounter.get(item2) >= threshold) {
            var hash = (item1 * uniqueItemsCount + item2) % bucketCount;
            if (itemPairCounter.get(hash) >= threshold) {
              var index = (item1 - 1) * (itemPairCounter.size() - item1 / 2) + item2 - item1;
              pairs.merge(index, 1, Integer::sum);
            }
          }
        }
      }
    }

    var P = pairs.size();
    out.println(A);
    out.println(P);
    pairs.values().stream()
        .sorted(Comparator.reverseOrder())
        .forEach(out::println);

    out.flush();
  }
}
