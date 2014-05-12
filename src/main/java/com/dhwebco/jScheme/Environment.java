package com.dhwebco.jScheme;

import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.ast.nodes.builtins.*;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, AstNode> symbols;

    public Environment() {
        symbols = new HashMap<>();
        this.setBuiltins();
    }

    public Environment(Map<String, AstNode> symbols) {
        this.symbols = symbols;
        this.setBuiltins();
    }

    private void setBuiltins() {
        this.symbols.put("+", new AdditionBuiltin());
        this.symbols.put("-", new SubtractionBuiltin());
        this.symbols.put("*", new MultiplicationBuiltin());
        this.symbols.put("/", new DivisionBuiltin());
        this.symbols.put("<", new LessThanBuiltin());
        this.symbols.put("exp", new ExponentBuiltin());
    }

    public void set(String name, AstNode value) throws Exception {
        if (symbols.containsKey(name)) {
            throw new Exception("Symbol " + name + " cannot be redefined");
        } else {
            this.symbols.put(name, value);
        }
    }
    public AstNode lookup(String name) throws Exception {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        } else {
            throw new Exception("Undefined symbol " + name);
        }
    }
}
