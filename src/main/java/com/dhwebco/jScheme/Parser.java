package com.dhwebco.jScheme;

import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.ast.nodes.*;

/*
    http://www.scheme.com/tspl2d/grammar.html
    This is not complete yet. Implementing a piece at a time. The number system in particular I am implementing
    in a drastically simpler fashion to being with.

    <expression>    ::= <constant> | (quote <datum>) | ' <datum> | (lambda <formals> <body>)
    <constant>      ::= <boolean> | <number> | <character> | <string>
    <boolean>       ::= #t | #f
    <number>        ::= <num 10>
    <num r>         ::= <complex r>
    <complex r>     ::= <real r>
    <real r>        ::= <ureal r>
    <ureal r>       ::= <uinteger r>
    <uinteger r>    ::= <digit r>+
    <digit 10>      ::= <digit>
    <digit>         ::= 0 | 1 | ... | 9
    <character>     ::= #\<any character> | #\newline | #\space
    <string>        ::= " <string character>* "
    <string character> ::= \" | \\ | <any character other than " or \>
    <datum>         ::= <boolean> | <number> | <character> | <string> | <symbol> | <list> | <vector>
    <symbol>        ::= <identifier>
    <identifier>    ::= <initial> <subsequent>* | + | - | ...
    <initial>       ::= <letter> | ! | $ | % | & | * | / | : | < | = | > | ? | ~ | _ | ^
    <subsequent>    ::= <initial> | <digit> | . | + | -
    <letter>		::= a | b | ... | z
    <list>          ::= (<datum>*)
    <vector>        ::= #(<datum>*)
    <formals>		::= <variable> | (<variable>*)
    <variable>      ::= <identifier>
    <body>		    ::= <definition>*(TODO) <expression>+
 */
public class Parser {
    private AstNode astRoot;
    private Tokenizer tokenizer;

    public Parser(String input) throws Exception {
        this.tokenizer = new Tokenizer(input);
        this.tokenizer.tokenize();
    }

    private void expression(AstNode parent) throws Exception {
        AstNode expr = new ExpressionNode();
        parent.addChild(expr);

        constant(expr);
        quote(expr);
        lambda(expr);
    }

    private void constant(AstNode parent) throws Exception {
        _boolean(parent);
        number(parent);
        character(parent);
        string(parent);
    }

    private void quote(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(") && tokenizer.peek(1).equals("quote")) {
            tokenizer.discardNextToken(2);
            AstNode node = new QuoteNode();
            parent.addChild(node);
            datum(node);
        } else if (tokenizer.peek().equals("'")) {
            tokenizer.discardNextToken();
            AstNode node = new QuoteNode();
            parent.addChild(node);
            datum(node);
        }
    }

    private void lambda(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(") && tokenizer.peek(1).equals("lambda")) {
            tokenizer.discardNextToken(2);
            AstNode node = new LambdaNode();
            formals(node);
            body(node);
            parent.addChild(node);
        }
    }

    private void formals(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(")) {
            tokenizer.discardNextToken();
            AstNode node = new ListNode();

            while (!tokenizer.endOfTokenStream() && !tokenizer.peek().equals(")")) {
                variable(node);
            }

            if (!tokenizer.peek().equals(")")) {
                throw new Exception("Unexpected end of list");
            } else {
                tokenizer.discardNextToken();
                parent.addChild(node);
            }
        } else {
            variable(parent);
        }
    }

    private void body(AstNode parent) throws Exception {
        expression(parent);
    }

    private void datum(AstNode parent) throws Exception {
        _boolean(parent);
        number(parent);
        character(parent);
        string(parent);
        symbol(parent);
        list(parent);
        vector(parent);
    }

    private void symbol(AstNode parent) {
        identifier(parent);
    }

    private void variable(AstNode parent) {
        identifier(parent);
    }

    private void identifier(AstNode parent) {
        boolean valid = false;
        if (initial() && subsequent()) {
            valid = true;
        } else if (tokenizer.peek().equals("+") || tokenizer.peek().equals("-") || tokenizer.peek().equals("...")) {
            valid = true;
        }

        if (valid) {
            AstNode node = new IdentifierNode(tokenizer.nextToken());
            parent.addChild(node);
        }
    }

    private boolean initial() {
        if (tokenizer.peek().length() > 0 && tokenizer.peek().substring(0, 1).matches("[A-Za-z!$%&*/:<=>?~_^]+")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean subsequent() {
        if (tokenizer.peek().matches("[A-Za-z0-9!$%&*/:<=>?~_^.+-]+")) {
            return true;
        } else {
            return false;
        }
    }

    private void list(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(")) {
            tokenizer.discardNextToken();
            AstNode node = new ListNode();
            while (!tokenizer.endOfTokenStream() && !tokenizer.peek().equals(")")) {
                datum(node);
            }
            if (!tokenizer.peek().equals(")")) {
                throw new Exception("Unexpected end of list");
            } else {
                tokenizer.discardNextToken();
                parent.addChild(node);
            }
        }
    }

    private void vector(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("#") && tokenizer.peek(1).equals("(")) {
            tokenizer.discardNextToken(2);
            AstNode node = new VectorNode();
            while (!tokenizer.endOfTokenStream() && !tokenizer.peek().equals(")")) {
                datum(node);
            }
            if (!tokenizer.peek().equals(")")) {
                throw new Exception("Unexpected end of vector");
            } else {
                tokenizer.discardNextToken();
                parent.addChild(node);
            }
        }
    }

    private void _boolean(AstNode parent) {
        if (tokenizer.peek().equalsIgnoreCase("#t")) {
            tokenizer.discardNextToken();
            AstNode node = new BooleanNode(true);
            parent.addChild(node);
        } else if (tokenizer.peek().equalsIgnoreCase("#f")) {
            tokenizer.discardNextToken();
            AstNode node = new BooleanNode(false);
            parent.addChild(node);
        }
    }

    private void number(AstNode parent) {
        num10(parent);
    }

    private void num10(AstNode parent) {
        complex10(parent);
    }

    private void complex10(AstNode parent) {
        real10(parent);
    }

    private void real10(AstNode parent) {
        ureal10(parent);
    }

    private void ureal10(AstNode parent) {
        uinteger10(parent);
    }

    private void uinteger10(AstNode parent) {
        digit10(parent);
    }

    private void digit10(AstNode parent) {
        if (tokenizer.peek().matches("[0-9]+")) {
            AstNode node = new IntegerNode(Integer.valueOf(tokenizer.nextToken()));
            parent.addChild(node);
        }
    }

    private void character(AstNode parent) throws Exception {
        if (tokenizer.peek().startsWith("#\\")) {
            String tok = tokenizer.nextToken();
            AstNode node;
            if (tok.equals("#\\newline")) {
                node = new CharacterNode('\n');
            } else if (tok.equals("#\\space")) {
                node = new CharacterNode(' ');
            } else if (tok.length() == 3) {
                node = new CharacterNode(tok.charAt(2));
            } else {
                throw new Exception("Invalid character sequence " + tok);
            }
            parent.addChild(node);
        }
    }

    private void string(AstNode parent) throws Exception {
        if (tokenizer.peek().startsWith("\"")) {
            AstNode node = new StringNode(tokenizer.nextToken());
            parent.addChild(node);
        }
    }



    public void parse() {
        try {
            // todo: program not form should be root
            astRoot = new FormNode();
            expression(astRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i =0;
    }

    public static void main(String[] args) {
        String s = "(quote (1 2 3))";
        try {
            Parser p = new Parser(s);
            p.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
