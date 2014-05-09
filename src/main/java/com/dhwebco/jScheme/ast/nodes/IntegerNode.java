package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.ast.AstNode;

public class IntegerNode extends AstNode {
    private Integer value;

    public IntegerNode(Integer value) {
        this.value = value;
    }
}
