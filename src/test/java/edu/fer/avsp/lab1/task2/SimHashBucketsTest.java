package edu.fer.avsp.lab1.task2;

import static org.junit.Assert.assertEquals;

import edu.fer.avsp.util.InputReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.junit.Test;

public class SimHashBucketsTest {

  @Test
  public void testWithProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    InputReader input = new InputReader(classLoader.getResourceAsStream("lab1/task2/first/R.in"));
    OutputStream myOutput = new ByteArrayOutputStream();

    new SimHashBuckets().solve(input, new PrintWriter(myOutput));

    InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab1/task2/first/R.out"));

    String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "");
    String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "");

    assertEquals(result, myResult);
  }

  @Test
  public void testWitchSecondProvidedExample() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    InputReader input = new InputReader(classLoader.getResourceAsStream("lab1/task2/second/R.in"));
    OutputStream myOutput = new ByteArrayOutputStream();

    new SimHashBuckets().solve(input, new PrintWriter(myOutput));
    InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab1/task2/second/R.out"));

    String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "");
    String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "");

    assertEquals(result, myResult);
  }

}