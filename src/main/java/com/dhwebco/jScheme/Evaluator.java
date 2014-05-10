package com.dhwebco.jScheme;

import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.ast.nodes.*;
import com.dhwebco.jScheme.values.*;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {
    AstNode root;

    public Evaluator(String program) throws Exception {
        Parser p = new Parser(program);
        root = p.getAstRoot();
    }

    public Value evaluate(Environment env) throws Exception {
        if (root == null) {
            return null;
        }

        return evaluate(root, env);
    }

    private Value evaluate(AstNode node, Environment env) throws Exception {
        if (node instanceof FormNode || node instanceof ProgramNode) {
            return evaluate(node.getChildren().get(0), env);
        } else if (node instanceof ExpressionNode) {
            return evaluate(node.getChildren().get(0), env);
        } else if (node instanceof BooleanNode
                || node instanceof IntegerNode
                || node instanceof StringNode
                || node instanceof CharacterNode) {
            return node.getValue();
        } else if (node instanceof QuoteNode) {
            return node.getChildren().get(0).getValue();
        } else if (node instanceof DefinitionNode) {
            List<AstNode> children = node.getChildren();
            if (children.size() > 2) {
                throw new Exception("Invalid arguments to define. Expected 2 got " + children.size());
            } else {
                AstNode identifier = children.get(0);
                if (identifier instanceof IdentifierNode) {
                    AstNode expression = children.get(1);
                    if (expression instanceof ExpressionNode) {
                        env.set(identifier.getValue().toString(), evaluate(expression, env));
                        return new NilValue();
                    } else {
                        throw new Exception("Invalid expression for define");
                    }
                } else {
                    throw new Exception("Invalid identifier for define");
                }
            }
        } else if (node instanceof IdentifierNode) {
            return env.lookup(node.getValue().toString());
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        Environment env = new Environment();
        env.set("x", new IntegerValue(12));
        Evaluator e = new Evaluator("(x)");
        Value v = e.evaluate(env);
        System.out.println(v.toString());
        int i = 0;
    }
}
