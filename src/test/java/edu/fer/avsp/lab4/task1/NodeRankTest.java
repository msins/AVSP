package edu.fer.avsp.lab4.task1;

import edu.fer.avsp.lab3.CF;
import edu.fer.avsp.util.InputReader;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class NodeRankTest {

    @Test
    public void testWithProvidedExample() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (int i = 1; i <= 4; i++) {
            InputReader input = new InputReader(classLoader.getResourceAsStream("lab4/task1/" + i + ".in"));
            OutputStream myOutput = new ByteArrayOutputStream();

            new NodeRank().solve(input, new PrintWriter(myOutput));
            InputReader correctOutput = new InputReader(classLoader.getResourceAsStream("lab4/task1/" + i + ".out"));

            String result = correctOutput.readAsString().replaceAll(System.lineSeparator(), "\n");
            String myResult = myOutput.toString().replaceAll(System.lineSeparator(), "\n");
            assertEquals(result, myResult);
        }
    }
}