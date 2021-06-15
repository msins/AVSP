package edu.fer.avsp.lab1.task5;

import java.util.HashSet;
import java.util.Set;

public class Node {
  int index;
  Set<Edge> edges;

  public Node(int index) {
    this.index = index;
    edges = new HashSet<>();
  }

  public void addEdge(Edge edge) {
    edges.add(edge);
  }

  public void removeEdge(Edge edge) {
    edges.remove(edge);
  }
}
