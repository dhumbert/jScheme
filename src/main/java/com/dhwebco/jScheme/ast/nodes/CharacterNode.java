package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.CharacterValue;

public class CharacterNode extends AstNode {
    private char value;

    public CharacterNode(char value) {
        this.value = value;
    }

    @Override
    public Value getValue() {
        return new CharacterValue(value);
    }
}
