package com.dhwebco.jScheme;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Value> symbols;

    public Environment() {
        symbols = new HashMap<>();
    }

    public Environment(Map<String, Value> symbols) {
        this.symbols = symbols;
    }

    public void set(String name, Value value) throws Exception {
        if (symbols.containsKey(name)) {
            throw new Exception("Symbol " + name + " cannot be redefined");
        } else {
            this.symbols.put(name, value);
        }
    }
    public Value lookup(String name) throws Exception {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        } else {
            throw new Exception("Undefined symbol " + name);
        }
    }
}
