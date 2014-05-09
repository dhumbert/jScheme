package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.ast.AstNode;

public class CharacterNode extends AstNode {
    private char value;

    public CharacterNode(char value) {
        this.value = value;
    }
}
