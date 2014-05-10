package com.dhwebco.jScheme.values;

import com.dhwebco.jScheme.Value;

public class CharacterValue extends Value {
    char value;

    public CharacterValue(char value) {
        this.value = value;
    }

    @Override
    public Character getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "#\\" + value;
    }
}
