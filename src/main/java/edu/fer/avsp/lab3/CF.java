package edu.fer.avsp.lab3;

import edu.fer.avsp.util.InputReader;
import edu.fer.avsp.util.Pair;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringJoiner;
import java.util.stream.IntStream;

public class CF {

  static final DecimalFormat formatter = new DecimalFormat("0.000");

  public static void main(String[] args) {
    InputReader in = new InputReader(System.in);
    PrintWriter out = new PrintWriter(System.out);

    new CF().solve(in, out);
  }

  public void solve(InputReader in, PrintWriter out) {
    var args = in.readLineTrimmed().split(" ");
    int n = Integer.parseInt(args[0]);
    int m = Integer.parseInt(args[1]);

    String[] lines = IntStream.range(0, n)
        .mapToObj(i -> in.readLineTrimmed())
        .toArray(String[]::new);

    CfMatrix cfMatrix = CfMatrix.parse(lines, n, m);

    int q = in.readInt();
    for (int i = 0; i < q; i++) {
      var queryArgs = Arrays.stream(in.readLineTrimmed().split("\\s+"))
          .mapToInt(Integer::parseInt)
          .toArray();
      int wantedRowIndex = queryArgs[0] - 1;
      int wantedColumnIndex = queryArgs[1] - 1;
      int type = queryArgs[2]; // if 0 item-item, if 1 user-user
      int cardinality = queryArgs[3];

      CfMatrix tempCfMatrix = cfMatrix.copy();

      if (type == 1) {
        tempCfMatrix = tempCfMatrix.transposed();

        int temp = wantedRowIndex;
        wantedRowIndex = wantedColumnIndex;
        wantedColumnIndex = temp;
      }

      var result = tempCfMatrix
          .predictValueAt(wantedRowIndex, wantedColumnIndex, cardinality);

      out.println(formatter.format(result));
    }

    out.flush();
  }

  private static class CfMatrix {

    private final double[][] matrix;
    private final double[][] normalisedMatrix;

    private CfMatrix(double[][] matrix) {
      this.matrix = matrix;
      this.normalisedMatrix = calculateNormalisedMatrix();
    }

    public static CfMatrix parse(String[] rows, int rowCount, int columnCount) {
      double[][] matrix = new double[rowCount][columnCount];
      for (int i = 0; i < rows.length; i++) {
        var rowValues = Arrays.stream(rows[i].split("\\s+"))
            .mapToDouble(CF::parseValue)
            .toArray();
        System.arraycopy(rowValues, 0, matrix[i], 0, rowValues.length);
      }
      return new CfMatrix(matrix);
    }

    private double[][] calculateNormalisedMatrix() {
      double[][] normalisedMatrix = new double[matrix[0].length][matrix.length];

      for (int i = 0; i < matrix.length; i++) {
        double rowSum = 0.0;
        int notNaNCount = 0;

        for (int j = 0; j < matrix[0].length; j++) {
          if (Double.isNaN(matrix[i][j])) {
            continue;
          }
          notNaNCount++;
          rowSum += matrix[i][j];
        }

        double mean = rowSum / notNaNCount;

        for (int j = 0; j < matrix[0].length; j++) {
          if (Double.isNaN(matrix[i][j])) {
            normalisedMatrix[i][j] = Double.NaN;
            continue;
          }

          normalisedMatrix[i][j] = matrix[i][j] - mean;
        }
      }

      return normalisedMatrix;
    }

    public double predictValueAt(int rowIndex, int columnIndex, int cardinality) {
      double[] similarities = calculateSimilaritiesToRow(rowIndex);

      int[] similarRowIndices = IntStream.range(0, similarities.length)
          .filter(index -> index != rowIndex) // ignore similarity to itself
          .filter(index -> similarities[index] > 0.0) // ignore negative similarities
          .filter(index -> !Double.isNaN(matrix[index][columnIndex])) // ignore items which have no score
          .mapToObj(index -> new Pair<>(index, similarities[index]))
          .sorted(Comparator.comparing(pair -> pair.second, Comparator.reverseOrder())) // sort by similarity descending
          .limit(cardinality)
          .mapToInt(pair -> pair.first) // map back to index
          .toArray();

      double ratingSimilarity = 0.0;

      for (int similarRowIndex : similarRowIndices) {
        if (!Double.isNaN(matrix[similarRowIndex][columnIndex])) {
          ratingSimilarity += matrix[similarRowIndex][columnIndex] * similarities[similarRowIndex];
        }
      }

      double sumOfSimilarities = Arrays.stream(similarRowIndices)
          .mapToDouble(i -> similarities[i])
          .sum();

      return ratingSimilarity / sumOfSimilarities;
    }

    private double[] calculateSimilaritiesToRow(int rowIndex) {
      double[] results = new double[normalisedMatrix.length];
      for (int i = 0; i < normalisedMatrix.length; i++) {
        // similarity to itself is 1
        if (i == rowIndex) {
          results[i] = 1.0;
          continue;
        }

        results[i] = cosineSimilarity(i, rowIndex);
      }
      return results;
    }

    private double cosineSimilarity(int rowIndex1, int rowIndex2) {
      double sum = 0.0;
      for (int j = 0; j < normalisedMatrix[0].length; j++) {
        double value1 = normalisedMatrix[rowIndex1][j];
        double value2 = normalisedMatrix[rowIndex2][j];

        if (Double.isNaN(value1) || Double.isNaN(value2)) {
          continue;
        }

        sum += value1 * value2;
      }

      double sumSquaresRow1 = 0.0;
      double sumSquaresRow2 = 0.0;
      for (int j = 0; j < normalisedMatrix[0].length; j++) {
        double value1 = normalisedMatrix[rowIndex1][j];
        double value2 = normalisedMatrix[rowIndex2][j];

        if (!Double.isNaN(value1)) {
          sumSquaresRow1 += Math.pow(normalisedMatrix[rowIndex1][j], 2);
        }

        if (!Double.isNaN(value2)) {
          sumSquaresRow2 += Math.pow(normalisedMatrix[rowIndex2][j], 2);
        }
      }

      return sum / Math.sqrt(sumSquaresRow1 * sumSquaresRow2);
    }

    // for switching between user-user, item-item mode
    public CfMatrix transposed() {
      double[][] transposed = new double[matrix[0].length][matrix.length];
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          transposed[j][i] = matrix[i][j];
        }
      }
      return new CfMatrix(transposed);
    }

    public CfMatrix copy() {
      return new CfMatrix(Arrays.stream(matrix)
          .map(double[]::clone)
          .toArray(double[][]::new)
      );
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", CfMatrix.class.getSimpleName() + "[", "]")
          .add("m=\n" + matrixToString(matrix))
          .add("cm=\n" + matrixToString(normalisedMatrix))
          .toString();
    }

    private String matrixToString(double[][] m) {
      StringBuilder sb = new StringBuilder();
      for (double[] row : m) {
        sb.append("[");
        for (int i = 0; i < m[0].length; i++) {
          sb.append(" ").append(formatter.format(row[i])).append(" ");
        }
        sb.append("]\n");
      }
      return sb.toString();
    }
  }

  static double parseValue(String value) {
    if (value.equals("X")) {
      return Double.NaN;
    }

    return Double.parseDouble(value);
  }
}