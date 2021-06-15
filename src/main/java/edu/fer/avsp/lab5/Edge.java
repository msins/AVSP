package edu.fer.avsp.lab5;

import java.util.Objects;

public class GnEdge {

  private WeightedEdge weightedEdge;
  private double betweenness;

  public GnEdge(WeightedEdge weightedEdge, double betweenness) {
    this.weightedEdge = Objects.requireNonNull(weightedEdge);
    this.betweenness = betweenness;
  }

  public GnEdge(WeightedEdge weightedEdge) {
    this.weightedEdge = Objects.requireNonNull(weightedEdge);
    this.betweenness = 0.0;
  }

  public int getWeight() {
    return weightedEdge.getWeight();
  }

  public AbstractEdge getEdge() {
    return weightedEdge.getEdge();
  }

  public double getBetweenness() {
    return betweenness;
  }

  public void setBetweenness(double betweenness) {
    this.betweenness = betweenness;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GnEdge)) {
      return false;
    }
    GnEdge gnEdge = (GnEdge) o;
    return weightedEdge.equals(gnEdge.weightedEdge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weightedEdge);
  }
}
