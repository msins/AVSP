package edu.fer.avsp.lab3;

import static org.junit.Assert.assertEquals;

import edu.fer.avsp.util.InputReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.junit.Test;

public class CfTest {

  @Test
  public void testWithProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    for (int i = 1; i <= 3; i++) {
      InputReader input = new InputReader(classLoader.getResourceAsStream("lab3/" + i + ".in"));
      OutputStream myOutput = new ByteArrayOutputStream();

      new CF().solve(input, new PrintWriter(myOutput));
      InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab3/" + i + ".out"));

      String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "\n");
      String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "\n");
      assertEquals(result, myResult);
    }
  }
}
