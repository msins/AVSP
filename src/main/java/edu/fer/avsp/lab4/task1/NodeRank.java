package edu.fer.avsp.lab4.task1;

import edu.fer.avsp.util.InputReader;
import edu.fer.avsp.util.Pair;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class NodeRank {

  static final int MAX_ITERATIONS = 100;
  static final DecimalFormat formatter = new DecimalFormat("0.0000000000");

  Map<Integer, List<Integer>> destinationMap = new HashMap<>();
  int[] degrees;

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new NodeRank().solve(in, out);
  }

  public void solve(InputReader in, PrintWriter out) {
    var args = in.readLineTrimmed().split(" ");
    int n = Integer.parseInt(args[0]);
    double beta = Double.parseDouble(args[1]);

    degrees = new int[n];
    for (int nodeIndex = 0; nodeIndex < n; nodeIndex++) {
      var destinations = Arrays.stream(in.readLineTrimmed().split("\\s+"))
                               .map(Integer::valueOf)
                               .toList();

      degrees[nodeIndex] = destinations.size();

      for (Integer destinationIndex : destinations) {
        destinationMap.computeIfAbsent(destinationIndex, __ -> new ArrayList<>()).add(nodeIndex);
      }
    }

    int q = in.readInt();
    var queries = IntStream.range(0, q)
                           .mapToObj(i -> in.readLineTrimmed().split("\\s+"))
                           .map(split -> new Pair<>(
                               Integer.parseInt(split[0]),
                               Integer.parseInt(split[1]))
                           )
                           .toList();

    double[][] ranks = new double[MAX_ITERATIONS + 1][n];
    Arrays.fill(ranks[0], 1.0 / n);

    for (int iteration = 1; iteration <= MAX_ITERATIONS; iteration++) {
      ranks[iteration] = doPowerIteration(ranks[iteration - 1], beta);
    }

    queries.forEach(query -> {
      int nodeIndex = query.first;
      int iteration = query.second;

      out.println(formatter.format(ranks[iteration][nodeIndex]));
    });

    out.flush();
  }

  double[] doPowerIteration(double[] prevRanks, double beta) {
    int n = prevRanks.length;
    double[] newRanks = new double[n];

    double s = 0;
    // rank only indexes that have an out degree (point to other nodes)
    var nodesWithOutDegree = destinationMap.keySet();
    for (int nodeIndex : nodesWithOutDegree) {
      double rank = 0;
      var destinations = destinationMap.get(nodeIndex);
      for (int destinationIndex : destinations) {
        rank += beta * prevRanks[destinationIndex] / degrees[destinationIndex];
      }
      newRanks[nodeIndex] = rank;
      s += rank;
    }

    for (int i = 0; i < n; i++) {
      newRanks[i] += (1.0 - s) / n;
    }

    return newRanks;
  }
}
