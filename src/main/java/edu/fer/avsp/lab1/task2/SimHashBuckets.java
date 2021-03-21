package edu.fer.avsp.lab1.task2;

import edu.fer.avsp.lab1.SimHashMD5;
import edu.fer.avsp.util.InputReader;
import edu.fer.avsp.util.Pair;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimHashBuckets {

  static int b = 8;
  static int r = 128 / b;
  BitSet[] simHashedTexts;
  List<Pair<Integer, Integer>> queries;
  Map<Integer, Set<Integer>> candidates;

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new SimHashBuckets().solve(in, out);
  }

  public void solve(InputReader in, PrintWriter out) {
    long start = System.currentTimeMillis();
    int n = in.readInt();
    simHashedTexts = IntStream.range(0, n)
        .mapToObj(i -> in.readLine().trim())
        .map(line -> new SimHashMD5(line).toBitSet())
        .toArray(BitSet[]::new);
    long time = System.currentTimeMillis() - start;
    System.out.println("simhash:" + (time) / 1000. + "s");

    start = System.currentTimeMillis();
    candidates = lsh(n);
    time = System.currentTimeMillis() - start;
    System.out.println("lsh: " + (time) / 1000. + "s");

    start = System.currentTimeMillis();
    int q = in.readInt();
    queries = IntStream.range(0, q)
        .mapToObj(i -> in.readLine().trim().split(" "))
        .map(split -> new Pair<>(
            Integer.parseInt(split[0]),
            Integer.parseInt(split[1]))
        )
        .collect(Collectors.toList());

    queries.forEach(pair -> out.println(findSimilarTexts(pair.first, pair.second)));
    time = System.currentTimeMillis() - start;
    System.out.println("queries: " + (time) / 1000. + "s");

    out.flush();
  }

  long findSimilarTexts(int originIdx, int maxDeviation) {
    return candidates.get(originIdx).stream()
        .filter(idx -> idx != originIdx)
        .map(textId -> hammingDistance(simHashedTexts[originIdx], simHashedTexts[textId]))
        .filter(distance -> distance <= maxDeviation)
        .count();
  }

  static int hash2int(int band, BitSet hash) {
    var bucketHash = hash.get(band * r, (band + 1) * r);
    //if we sliced band which contains only zeros, toLongArray()[0] will throw array out of index
    if (bucketHash.length() == 0) {
      return 0;
    }
    // bitset is implemented with long[] under the hood, band size is only 16 so casting won't cause problems
    return (int) bucketHash.toLongArray()[0];
  }

  public Map<Integer, Set<Integer>> lsh(int n) {
    Map<Integer, Set<Integer>> candidates = new HashMap<>();
    for (int band = 0; band < b; band++) {
      var buckets = new HashMap<Integer, Set<Integer>>(r);
      for (int currentTextId = 0; currentTextId < n; currentTextId++) {
        var hash = simHashedTexts[currentTextId];
        int val = hash2int(band, hash);

        Set<Integer> textIdsInBucket;
        if (buckets.containsKey(val)) {
          textIdsInBucket = buckets.get(val);
          for (var textId : textIdsInBucket) {
            candidates.computeIfAbsent(currentTextId, k -> new HashSet<>()).add(textId);
            candidates.computeIfAbsent(textId, k -> new HashSet<>()).add(currentTextId);
          }
        } else {
          textIdsInBucket = new HashSet<>();
        }
        textIdsInBucket.add(currentTextId);
        buckets.put(val, textIdsInBucket);
      }
    }

    return candidates;
  }

  static int hammingDistance(BitSet x, BitSet y) {
    if (x.size() != y.size()) {
      throw new AssertionError();
    }

    int dist = 0;
    for (int i = 0; i < x.size(); i++) {
      if (x.get(i) != y.get(i)) {
        dist++;
      }
    }

    return dist;
  }

}
