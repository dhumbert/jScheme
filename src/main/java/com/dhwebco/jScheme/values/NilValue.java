package com.dhwebco.jScheme.values;

import com.dhwebco.jScheme.Value;

public class NilValue extends Value {
    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "nil";
    }
}
