package com.dhwebco.jScheme;

import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.ast.nodes.*;
import com.dhwebco.jScheme.values.BooleanValue;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParseBoolean() throws Exception {
        Parser p = new Parser("#t");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node instanceof ProgramNode);
        Assert.assertTrue(node.getChild() instanceof FormNode);
        Assert.assertTrue(node.getChild().getChild() instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof BooleanNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getValue().equals(true));

        Parser pf = new Parser("#f");
        AstNode fnode = pf.getAstRoot();
        Assert.assertTrue(fnode.getChild().getChild().getChild().getValue().equals(false));
    }

    @Test
    public void testParseNumber() throws Exception {
        Parser p = new Parser("12");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof IntegerNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getValue().equals(12));
    }

    @Test
     public void testParseString() throws Exception {
        Parser p = new Parser("\"Hello\"");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof StringNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getValue().equals("Hello"));
    }

    @Test
    public void testParseEscapedQuoteInString() throws Exception {
        String s = "\"a\\\\\\\"\"";
        Parser p = new Parser(s);
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof StringNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getValue().equals("a\\\""));
    }

    @Test
    public void testInvalidEscapeSequenceInString() throws Exception {
        boolean thrown = false;
        try {
            new Parser("\"lorem \\x ipsum\"");
        } catch (Exception e) {
            if (e.getMessage().contains("Invalid escape sequence in string")) {
                thrown = true;
            }
        }

        Assert.assertTrue(thrown);
    }

    public void testUnescapedQuoteInString() throws Exception {
        new Parser("\"hello \"\"");
    }

    @Test
    public void testParseQuotedValue() throws Exception {
        Parser p = new Parser("(quote 45)");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof QuoteNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChild() instanceof IntegerNode);
    }

    @Test
    public void testParseList() throws Exception {
        Parser p = new Parser("'(1 2 3 #\\z \"ipsum\" \"dolor\")");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild().getChild() instanceof ListNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChild().getChildren().size() == 6);
    }

    @Test
    public void testParseVector() throws Exception {
        Parser p = new Parser("(quote #(1 2 3 #\\a \"lorem\"))");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild().getChild() instanceof VectorNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChild().getChildren().size() == 5);
    }

    @Test
    public void testParseApplication() throws Exception {
        Parser p = new Parser("(filter even? (range 1 5))");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild() instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(0) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(0).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(0).getChild().getValue().equals("filter"));
        Assert.assertTrue(node.getChild().getChild().getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(1).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(1).getChild().getValue().equals("even?"));
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(0) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(0).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(0).getChild().getValue().equals("range"));
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(1).getChild() instanceof IntegerNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(1).getChild().getValue().equals(1));
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(2) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(2).getChild() instanceof IntegerNode);
        Assert.assertTrue(node.getChild().getChild().getChildren().get(2).getChildren().get(2).getChild().getValue().equals(5));
    }

    @Test
    public void testParseLambdaWithSingleParam() throws Exception {
        Parser p = new Parser("(lambda num (* num 13))");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof LambdaNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChild() instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChild().getChildren().size() == 1);
        Assert.assertTrue(node.getChild().getChild().getChild().getChild().getChild().getValue().equals("num"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().size() == 3);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(0) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(0).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(0).getChild().getValue().equals("*"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(1).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(1).getChild().getValue().equals("num"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChild() instanceof IntegerNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChild().getValue().equals(13));
    }

    @Test
    public void testParseLambdaWithMultipleParams() throws Exception {
        Parser p = new Parser("(lambda (num1 num2 num3) (+ num1 (- num2 num3)))");
        AstNode node = p.getAstRoot();
        Assert.assertTrue(node.getChild().getChild().getChild() instanceof LambdaNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0).getChildren().get(0) instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0).getChildren().get(0).getValue().equals("num1"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0).getChildren().get(1) instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0).getChildren().get(1).getValue().equals("num2"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0).getChildren().get(2) instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(0).getChildren().get(2).getValue().equals("num3"));

        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(0) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(0).getChildren().get(0) instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(0).getChildren().get(0).getValue().equals("+"));

        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(1).getChildren().get(0) instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(1).getChildren().get(0).getValue().equals("num1"));

        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(0) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(0).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(0).getChild().getValue().equals("-"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(1) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(1).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(1).getChild().getValue().equals("num2"));
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(2) instanceof ExpressionNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(2).getChild() instanceof IdentifierNode);
        Assert.assertTrue(node.getChild().getChild().getChild().getChildren().get(1).getChildren().get(2).getChildren().get(2).getChild().getValue().equals("num3"));
    }
}
