package edu.fer.avsp.lab1.task1;

import static org.junit.Assert.assertEquals;

import edu.fer.avsp.lab1.SimHashMD5;
import edu.fer.avsp.util.InputReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.junit.Test;

public class SimHashTest {

  @Test
  public void testGivenExample() {
    SimHashMD5 simHash = new SimHashMD5("fakultet elektrotehnike i racunarstva");
    assertEquals(simHash.toHexString(), "f27c6b49c8fcec47ebeef2de783eaf57");

  }

  @Test(timeout = 20_000)
  public void testWithProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    InputReader input = new InputReader(classLoader.getResourceAsStream("lab1/task1/R.in"));
    OutputStream myOutput = new ByteArrayOutputStream();

    new SimHash().solve(input, new PrintWriter(myOutput));

    InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab1/task1/R.out"));

    String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "");
    String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "");
    assertEquals(result, myResult);
  }
}