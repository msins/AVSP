package edu.fer.avsp.lab1.task1;

import edu.fer.avsp.lab1.SimHashMD5;
import edu.fer.avsp.util.InputReader;
import edu.fer.avsp.util.Pair;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimHash {

  BitSet[] simHashedTexts;
  List<Pair<Integer, Integer>> queries;

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new SimHash().solve(in, out);
  }

  public void solve(InputReader in, PrintWriter out) {
    int n = in.readInt();
    simHashedTexts = IntStream.range(0, n)
        .mapToObj(i -> in.readLineTrimmed())
        .map(line -> new SimHashMD5(line).toBitSet())
        .toArray(BitSet[]::new);

    int q = in.readInt();
    queries = IntStream.range(0, q)
        .mapToObj(i -> in.readLineTrimmed().split(" "))
        .map(split -> new Pair<>(
            Integer.parseInt(split[0]),
            Integer.parseInt(split[1]))
        )
        .collect(Collectors.toList());

    queries.forEach(pair -> out.println(findSimilarTexts(pair.first, pair.second)));
    out.flush();
  }

  long findSimilarTexts(int originIdx, int maxDeviation) {
    var origin = simHashedTexts[originIdx];
    return Arrays.stream(simHashedTexts)
        .filter(bitset -> bitset != origin)
        .map(simHash -> hammingDistance(origin, simHash))
        .filter(distance -> distance <= maxDeviation)
        .count();
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
