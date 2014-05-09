package com.dhwebco.jScheme;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private String input;
    private List<String> tokens;
    private int pos;

    public Tokenizer(String input) {
        this.pos = 0;
        this.input = input;
        this.tokens = new ArrayList<>();
    }

    public void tokenize() throws Exception {
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

    public String peek() {
        try {
            return tokens.get(pos);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public String peek(int offset) {
        try {
            return tokens.get(pos + offset);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public boolean endOfTokenStream() {
        return pos == tokens.size();
    }

    public String nextToken() {
        try {
            return tokens.get(pos++);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public void discardNextToken() {
        nextToken();
    }

    public void discardNextToken(int number) {
        for (int i = 0; i < number; i++) {
            nextToken();
        }
    }
}
