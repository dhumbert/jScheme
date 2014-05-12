package com.dhwebco.jScheme;

import org.junit.Assert;
import org.junit.Test;

public class EvaluatorTest {
    @Test
    public void testAddition() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(+ 1 2)");
        Value v = e.evaluate(env);
        Assert.assertEquals(3, v.getValue());
    }

    @Test
    public void testSubtraction() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(- 100 26)");
        Value v = e.evaluate(env);
        Assert.assertEquals(74, v.getValue());
    }

    @Test
    public void testMultiplication() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(* 9 20)");
        Value v = e.evaluate(env);
        Assert.assertEquals(180, v.getValue());
    }

    @Test
    public void testDivision() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(/ 81 9)");
        Value v = e.evaluate(env);
        Assert.assertEquals(9, v.getValue());
    }

    @Test
    public void testLessThanTrue() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(< 1 3)");
        Value v = e.evaluate(env);
        Assert.assertEquals(true, v.getValue());
    }

    @Test
    public void testLessThanFalse() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(< 3 1)");
        Value v = e.evaluate(env);
        Assert.assertEquals(false, v.getValue());
    }

    @Test
    public void testExponent() throws Exception {
        Environment env = new Environment();
        Evaluator e = new Evaluator("(exp 3 2)");
        Value v = e.evaluate(env);
        Assert.assertEquals(9, v.getValue());
    }
}
