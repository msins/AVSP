package edu.fer.avsp.lab2;

import static org.junit.Assert.assertEquals;

import edu.fer.avsp.util.InputReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.junit.Test;

public class PCYTest {

  @Test
  public void testWithProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    InputReader input = new InputReader(classLoader.getResourceAsStream("lab2/R.in"));
    OutputStream myOutput = new ByteArrayOutputStream();

    new PCY().solve(input, new PrintWriter(myOutput));

    InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab2/R.out"));

    String result = correctOutput.readAsString();
    String myResult = myOutput.toString();

    assertEquals(result, myResult);
  }
}