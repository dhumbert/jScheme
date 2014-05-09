package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.ast.AstNode;

public class BooleanNode extends AstNode {
    private boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }
}
