package edu.fer.avsp.lab6;

import static org.junit.Assert.assertEquals;

import edu.fer.avsp.util.InputReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.junit.Test;

public class DgimTest {

  @Test
  public void testWithProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    for (int i = 1; i <= 9; i++) {
      InputReader input = new InputReader(classLoader.getResourceAsStream("lab6/" + i + ".in"));
      OutputStream myOutput = new ByteArrayOutputStream();

      new Dgim().solve(input, new PrintWriter(myOutput));
      InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab6/" + i + ".out"));

      String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "\n");
      String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "\n");
      assertEquals(result, myResult);
    }
  }

}