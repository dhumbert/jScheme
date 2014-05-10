package com.dhwebco.jScheme;

import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.ast.nodes.*;

/*
    http://www.scheme.com/tspl2d/grammar.html
    This is not complete yet. Implementing a piece at a time. The number system in particular I am implementing
    in a drastically simpler fashion to being with.

    <form>          ::= <definition> | <expression>
    <definition>    ::= <variable definition>
    <variable definition> ::= (define <variable> <expression>)
    <expression>    ::= <constant> | (quote <datum>) | ' <datum> | (lambda <formals> <body>) | <application>
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
    <application>   ::= (<expression> <expression>*)
 */
public class Parser {
    private AstNode astRoot;
    private Tokenizer tokenizer;

    public Parser(String input) throws Exception {
        this.tokenizer = new Tokenizer(input);
        this.tokenizer.tokenize();
        this.parse();
    }

    private boolean form(AstNode parent) throws Exception {
        FormNode node = new FormNode();
        boolean definition = definition(node);
        boolean expression = expression(node);

        if (definition || expression) {
            parent.addChild(node);
            return true;
        }
        return false;
    }

    private boolean definition(AstNode parent) throws Exception {
        boolean variableDefinition = variableDefinition(parent);

        return variableDefinition;
    }

    private boolean variableDefinition(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(") && tokenizer.peek(1).equals("define")) {
            tokenizer.discardNextToken(2);

            AstNode node = new DefinitionNode();
            boolean variable = variable(node);
            if (!variable) {
                throw new Exception("Invalid variable name for definition");
            }

            boolean expression = expression(node);
            if (!expression) {
                throw new Exception("Invalid expression for definition");
            }

            if (!tokenizer.peek().equals(")")) {
                throw new Exception("Missing closing parenthesis for definition");
            } else {
                tokenizer.discardNextToken();
                parent.addChild(node);
                return true;
            }
        }

        return false;
    }

    private boolean expression(AstNode parent) throws Exception {
        AstNode expressionNode = new ExpressionNode();

        if (constant(expressionNode) || variable(expressionNode) || quote(expressionNode) || lambda(expressionNode) || application(parent)) {
            if (expressionNode.hasChildren()) { // not application, leads to too many nested expressions
                parent.addChild(expressionNode);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean constant(AstNode parent) throws Exception {
        boolean _boolean = _boolean(parent);
        boolean number = number(parent);
        boolean character = character(parent);
        boolean string = string(parent);

        return _boolean || number || character || string;
    }

    private boolean quote(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(") && tokenizer.peek(1).equals("quote")) {
            tokenizer.discardNextToken(2);
            AstNode node = new QuoteNode();
            parent.addChild(node);
            datum(node);
            if (tokenizer.peek().equals(")")) {
                tokenizer.discardNextToken();
            } else {
                throw new Exception("Unexpected end of quote form");
            }
            return true;
        } else if (tokenizer.peek().equals("'")) {
            tokenizer.discardNextToken();
            AstNode node = new QuoteNode();
            parent.addChild(node);
            datum(node);
            return true;
        }
        return false;
    }

    private boolean lambda(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(") && tokenizer.peek(1).equals("lambda")) {
            tokenizer.discardNextToken(2);
            AstNode node = new LambdaNode();
            formals(node);
            body(node);

            if (tokenizer.peek().equals(")")) {
                tokenizer.discardNextToken();
                parent.addChild(node);
                return true;
            } else {
                throw new Exception("Missing closing parenthesis for lambda");
            }
        }
        return false;
    }

    private boolean formals(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(")) {
            tokenizer.discardNextToken();
            AstNode node = new ExpressionNode();

            while (!tokenizer.endOfTokenStream() && !tokenizer.peek().equals(")") && variable(node)) {
                int i = 9;
            }

            if (!tokenizer.peek().equals(")")) {
                throw new Exception("Unexpected end of lambda parameter list");
            } else {
                tokenizer.discardNextToken();
                parent.addChild(node);
                return true;
            }
        } else {
            return variable(parent);
        }
    }

    private boolean body(AstNode parent) throws Exception {
        return expression(parent);
    }

    private boolean application(AstNode parent) throws Exception {
        if (tokenizer.peek().equals("(")) {
            tokenizer.discardNextToken();
            AstNode node = new ExpressionNode();
            // there must be at least one expression
            if (!expression(node)) {
                return false;
            }

            while (!tokenizer.endOfTokenStream() && !tokenizer.peek().equals(")") && expression(node)) {
                int i = 0;
            }

            if (!tokenizer.peek().equals(")")) {
                throw new Exception("Unexpected end of list");
            } else {
                tokenizer.discardNextToken();
                parent.addChild(node);
                return true;
            }
        }
        return false;
    }

    private boolean datum(AstNode parent) throws Exception {
        boolean _boolean = _boolean(parent);
        boolean number = number(parent);
        boolean character = character(parent);
        boolean string = string(parent);
        boolean symbol = symbol(parent);
        boolean list = list(parent);
        boolean vector = vector(parent);
        return _boolean || number || character || string || symbol || list || vector;
    }

    private boolean symbol(AstNode parent) {
        return identifier(parent);
    }

    private boolean variable(AstNode parent) {
        return identifier(parent);
    }

    private boolean identifier(AstNode parent) {
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
        return valid;
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

    private boolean list(AstNode parent) throws Exception {
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
                return true;
            }
        }
        return false;
    }

    private boolean vector(AstNode parent) throws Exception {
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
                return true;
            }
        }
        return false;
    }

    private boolean _boolean(AstNode parent) {
        if (tokenizer.peek().equalsIgnoreCase("#t")) {
            tokenizer.discardNextToken();
            AstNode node = new BooleanNode(true);
            parent.addChild(node);
            return true;
        } else if (tokenizer.peek().equalsIgnoreCase("#f")) {
            tokenizer.discardNextToken();
            AstNode node = new BooleanNode(false);
            parent.addChild(node);
            return true;
        }
        return false;
    }

    private boolean number(AstNode parent) {
        return num10(parent);
    }

    private boolean num10(AstNode parent) {
        return complex10(parent);
    }

    private boolean complex10(AstNode parent) {
        return real10(parent);
    }

    private boolean real10(AstNode parent) {
        return ureal10(parent);
    }

    private boolean ureal10(AstNode parent) {
        return uinteger10(parent);
    }

    private boolean uinteger10(AstNode parent) {
        return digit10(parent);
    }

    private boolean digit10(AstNode parent) {
        if (tokenizer.peek().matches("[0-9]+")) {
            AstNode node = new IntegerNode(Integer.valueOf(tokenizer.nextToken()));
            parent.addChild(node);
            return true;
        }
        return false;
    }

    private boolean character(AstNode parent) throws Exception {
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
            return true;
        }
        return false;
    }

    private boolean string(AstNode parent) throws Exception {
        if (tokenizer.peek().startsWith("\"") && tokenizer.peek().endsWith("\"")) {
            String token = tokenizer.nextToken();
            token = token.substring(1, token.length() - 1); // remove start and end quotes
            AstNode node = new StringNode(token);
            parent.addChild(node);
            return true;
        }
        return false;
    }

    private void parse() {
        try {
            astRoot = new ProgramNode();
            form(astRoot);

            if (!tokenizer.endOfTokenStream()) {
                throw new Exception("Unexpected token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i =0;
    }

    public AstNode getAstRoot() {
        return astRoot;
    }

    public static void main(String[] args) {
        String s = "\"a\\\\\\\"\"";
        try {
            Parser p = new Parser(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
