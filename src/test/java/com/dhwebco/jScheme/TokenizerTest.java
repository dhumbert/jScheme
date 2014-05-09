package com.dhwebco.jScheme;

import org.junit.Assert;
import org.junit.Test;

public class TokenizerTest {
    @Test
    public void testSimple() throws Exception {
        Tokenizer t = new Tokenizer("a b c");
        t.tokenize();
        Assert.assertEquals("a", t.nextToken());
        Assert.assertEquals("b", t.nextToken());
        Assert.assertEquals("c", t.nextToken());
        Assert.assertTrue(t.endOfTokenStream());
    }

    @Test
    public void testString() throws Exception {
        Tokenizer t = new Tokenizer("a b \"Hello world\"");
        t.tokenize();
        t.discardNextToken(2);
        Assert.assertEquals("\"Hello world\"", t.nextToken());
        Assert.assertTrue(t.endOfTokenStream());
    }

    @Test
    public void testParens() throws Exception {
        Tokenizer t = new Tokenizer("(lorem 12 (ipsum (dolor)))");
        t.tokenize();
        Assert.assertEquals("(", t.nextToken());
        Assert.assertEquals("lorem", t.nextToken());
        Assert.assertEquals("12", t.nextToken());
        Assert.assertEquals("(", t.nextToken());
        Assert.assertEquals("ipsum", t.nextToken());
        Assert.assertEquals("(", t.nextToken());
        Assert.assertEquals("dolor", t.nextToken());
        Assert.assertEquals(")", t.nextToken());
        Assert.assertEquals(")", t.nextToken());
        Assert.assertEquals(")", t.nextToken());
        Assert.assertTrue(t.endOfTokenStream());
    }
}
