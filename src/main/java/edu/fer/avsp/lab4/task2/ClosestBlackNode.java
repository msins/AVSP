package edu.fer.avsp.lab4.task2;

import edu.fer.avsp.util.InputReader;
import edu.fer.avsp.util.Pair;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;

enum NodeColor {
  WHITE, BLACK
}

final class Node implements Comparable<Node> {
  private final int index;
  private final NodeColor color;
  private final List<Node> neighbours = new ArrayList<>();

  private Node(int index, NodeColor color) {
    this.index = index;
    this.color = color;
  }

  static Node fromIndexAndColor(int index, NodeColor color) {
    return new Node(index, color);
  }

  int getIndex() {
    return index;
  }

  boolean isOfColor(NodeColor color) {
    return this.color == color;
  }

  boolean isBlack() {
    return isOfColor(NodeColor.BLACK);
  }

  void addNeighbour(Node node) {
    neighbours.add(node);
  }

  Pair<Integer, Node> findFirstNodeWhere(Predicate<Node> condition) {
    Queue<Pair<Integer, Node>> open = new PriorityQueue<>();
    Set<Node> visited = new HashSet<>();
    open.add(new Pair<>(0, this));

    while (!open.isEmpty()) {
      var current = open.remove();

      int distance = current.first;
      Node node = current.second;
      if (condition.test(node)) {
        return current;
      }

      visited.add(node);
      for (Node neighbour : node.neighbours) {
        if (!visited.contains(neighbour)) {
          open.add(new Pair<>(distance + 1, neighbour));
        }
      }
    }

    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Node node = (Node) o;

    return index == node.index;
  }

  @Override
  public int hashCode() {
    return index;
  }

  @Override
  public int compareTo(Node other) {
    return Comparator.comparingInt(Node::getIndex)
                     .compare(this, other);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Node.class.getSimpleName() + "[", "]")
        .add("index=" + index)
        .add("color=" + color)
        .toString();
  }
}

public class ClosestBlackNode {

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new ClosestBlackNode().solve(in, out);
  }

  public void solve(InputReader in, PrintWriter out) {
    var args = in.readLineTrimmed().split(" ");
    int n = Integer.parseInt(args[0]);
    double e = Integer.parseInt(args[1]);

    Node[] nodes = new Node[n];
    for (int i = 0; i < n; i++) {
      NodeColor nodeColor = in.readInt() == 0 ? NodeColor.WHITE : NodeColor.BLACK;
      nodes[i] = Node.fromIndexAndColor(i, nodeColor);
    }

    for (int i = 0; i < e; i++) {
      var vertices = in.readLineTrimmed().split(" ");
      int nodeAIndex = Integer.parseInt(vertices[0]);
      int nodeBIndex = Integer.parseInt(vertices[1]);
      Node nodeA = nodes[nodeAIndex];
      Node nodeB = nodes[nodeBIndex];

      nodeA.addNeighbour(nodeB);
      nodeB.addNeighbour(nodeA);
    }

    for (Node node : nodes) {
      var result = node.findFirstNodeWhere(Node::isBlack);
      if (result == null) {
        out.println("-1 -1");
        continue;
      }

      int distance = result.first;
      Node nearest = result.second;

      out.println(nearest.getIndex() + " " + distance);
    }

    out.flush();
  }
}
