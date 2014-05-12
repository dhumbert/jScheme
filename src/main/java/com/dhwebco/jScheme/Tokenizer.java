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
                    token.append(c); i++;

                    while (i < input.length() && input.charAt(i) != '"') {
                        if (input.charAt(i) == '\\') {
                            if (i + 1 < input.length() && input.charAt(i + 1) == '"') {
                                token.append("\"");
                                i += 2;
                            } else if (i + 1 < input.length() && input.charAt(i + 1) == '\\') {
                                token.append("\\");
                                i += 2;
                            } else {
                                throw new Exception("Invalid escape sequence in string");
                            }
                        } else {
                            token.append(input.charAt(i));
                            i++;
                        }
                    }

                    if (i >= input.length() || input.charAt(i) != '"') {
                        throw new Exception("No closing quote for string " + token.toString());
                    } else {
                        token.append('"');
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
            return null;
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
