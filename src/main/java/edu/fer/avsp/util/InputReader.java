package edu.fer.avsp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputReader {

  public BufferedReader in;

  public InputReader(InputStream inputStream) {
    in = new BufferedReader(new InputStreamReader(inputStream));
  }

  public int readInt() {
    try {
      return Integer.parseInt(in.readLine().trim());
    } catch (IOException cantHappen) {
      throw new RuntimeException();
    }
  }

  public String readLine() {
    try {
      return in.readLine().trim();
    } catch (IOException ignored) {
      throw new RuntimeException();
    }
  }

  public String readAsString() {
    StringBuilder sb = new StringBuilder();
    try {
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line.trim());
      }
    } catch (IOException ignored) {
    }

    return sb.toString();
  }
}
