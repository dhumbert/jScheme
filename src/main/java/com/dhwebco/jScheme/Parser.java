package com.dhwebco.jScheme;

import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.ast.types.*;

import java.util.ArrayList;

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
    private int pos;
    private String input;
    private ArrayList<String> tokens;
    private AstNode astRoot;

    public Parser(String input) {
        this.input = input;
        this.pos = 0;
        this.tokens = new ArrayList<>();
    }

    private void tokenize() throws Exception {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (!isWhitespace(c)) {
                if (c == '"') { // string
                    StringBuilder token = new StringBuilder();
                    token.append(c);

                    while (i + 1 < input.length() && isValidStringChar(input.charAt(i + 1), input.charAt(i))) {
                        i++;
                        token.append(input.charAt(i));
                    }

                    if (input.charAt(i + 1) != '"') {
                        throw new Exception("No closing quote for string " + token.toString());
                    } else {
                        token.append('"');
                        i++; // eat closing quote
                    }

                    tokens.add(token.toString());
                }
                else if (isValidAtomChar(c)) {
                    StringBuilder token = new StringBuilder();
                    token.append(c);

                    while (i + 1 < input.length() && isValidAtomChar(input.charAt(i + 1))) {
                        i++;
                        token.append(input.charAt(i));
                    }

                    tokens.add(token.toString());
                } else {
                    tokens.add(String.valueOf(c));
                }
            }
        }
    }

    private void expression(AstNode parent) throws Exception {
        AstNode<Expression> expr = new AstNode<>(new Expression());
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
        if (peek().equals("(") && peek(1).equals("quote")) {
            discardNextToken(2);
            AstNode<Quote> q = new AstNode<>(new Quote());
            parent.addChild(q);
            datum(q);
        } else if (peek().equals("'")) {
            discardNextToken();
            AstNode<Quote> q = new AstNode<>(new Quote());
            parent.addChild(q);
            datum(q);
        }
    }

    private void lambda(AstNode parent) throws Exception {
        if (peek().equals("(") && peek(1).equals("lambda")) {
            discardNextToken(2);
            AstNode<Lambda> node = new AstNode<>(new Lambda());
            formals(node);
            body(node);
            parent.addChild(node);
        }
    }

    private void formals(AstNode parent) {
        if (peek().equals("(")) {

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
        } else if (peek().equals("+") || peek().equals("-") || peek().equals("...")) {
            valid = true;
        }

        if (valid) {
            AstNode<Identifier> node = new AstNode<>(new Identifier(nextToken()));
            parent.addChild(node);
        }
    }

    private boolean initial() {
        if (peek().length() > 0 && peek().substring(0, 1).matches("[A-Za-z!$%&*/:<=>?~_^]+")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean subsequent() {
        if (peek().matches("[A-Za-z0-9!$%&*/:<=>?~_^.+-]+")) {
            return true;
        } else {
            return false;
        }
    }

    private void list(AstNode parent) throws Exception {
        if (peek().equals("(")) {
            discardNextToken();
            AstNode<List> node = new AstNode<>(new List());
            while (!endOfTokenStream() && !peek().equals(")")) {
                datum(node);
            }
            if (!peek().equals(")")) {
                throw new Exception("Unexpected end of list");
            } else {
                discardNextToken();
                parent.addChild(node);
            }
        }
    }

    private void vector(AstNode parent) throws Exception {
        if (peek().equals("#") && peek(1).equals("(")) {
            discardNextToken(2);
            AstNode<Vector> node = new AstNode<>(new Vector());
            while (!endOfTokenStream() && !peek().equals(")")) {
                datum(node);
            }
            if (!peek().equals(")")) {
                throw new Exception("Unexpected end of vector");
            } else {
                discardNextToken();
                parent.addChild(node);
            }
        }
    }

    private void _boolean(AstNode parent) {
        if (peek().equalsIgnoreCase("#t")) {
            discardNextToken();
            AstNode<Boolean> node = new AstNode<>(true);
            parent.addChild(node);
        } else if (peek().equalsIgnoreCase("#f")) {
            discardNextToken();
            AstNode<Boolean> node = new AstNode<>(false);
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
        if (peek().matches("[0-9]+")) {
            AstNode<Integer> node = new AstNode<>(Integer.valueOf(nextToken()));
            parent.addChild(node);
        }
    }

    private void character(AstNode parent) throws Exception {
        if (peek().startsWith("#\\")) {
            String tok = nextToken();
            AstNode<Character> node;
            if (tok.equals("#\\newline")) {
                node = new AstNode<>('\n');
            } else if (tok.equals("#\\space")) {
                node = new AstNode<>(' ');
            } else if (tok.length() == 3) {
                node = new AstNode<>(tok.charAt(2));
            } else {
                throw new Exception("Invalid character sequence " + tok);
            }
            parent.addChild(node);
        }
    }

    private void string(AstNode parent) throws Exception {
        if (peek().startsWith("\"")) {
            AstNode<String> node = new AstNode<>(nextToken());
            parent.addChild(node);
        }
    }

    private boolean isValidStringChar(char c, char prevChar) {
        if (c == '"' && prevChar != '\\') {
            return false;
        } else if (c == '\\' && prevChar != '\\') {
            return false;
        }

        return true;
    }

    private boolean isValidAtomChar(char c) {
        return !isWhitespace(c) && c != '(' && c != ')'
                && c != '[' && c != ']' && c != '"' && c != '\'';
    }

    private boolean isValidAtom(String s) {
        for (char c : s.toCharArray()) {
            if (!isValidAtomChar(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\t';
    }

    private String peek() {
        try {
            return tokens.get(pos);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String peek(int offset) {
        try {
            return tokens.get(pos + offset);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private boolean endOfTokenStream() {
        return pos == tokens.size();
    }

    private String nextToken() {
        try {
            return tokens.get(pos++);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private void discardNextToken() {
        nextToken();
    }

    private void discardNextToken(int number) {
        for (int i = 0; i < number; i++) {
            nextToken();
        }
    }

    public void parse() {
        try {
            tokenize();

            // todo: program not form should be root
            astRoot = new AstNode<>(new Form());
            expression(astRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i =0;
    }

    public static void main(String[] args) {
        String s = "(lambda x 1)";
        Parser p = new Parser(s);
        p.parse();
    }
}
