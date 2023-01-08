package edu.fer.avsp.lab4.task2;

import static org.junit.Assert.assertEquals;

import edu.fer.avsp.util.InputReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.junit.Test;

public class ClosestBlackNodeTest {

  @Test
  public void testWithProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    for (int i = 1; i <= 4; i++) {
      InputReader input = new InputReader(classLoader.getResourceAsStream("lab4/task2/" + i + ".in"));
      OutputStream myOutput = new ByteArrayOutputStream();

      new ClosestBlackNode().solve(input, new PrintWriter(myOutput));
      InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab4/task2/" + i + ".out"));

      String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "\n");
      String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "\n");
      assertEquals(result, myResult);
    }
  }
}
