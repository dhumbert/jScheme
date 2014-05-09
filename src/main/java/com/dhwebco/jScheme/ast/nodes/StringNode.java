package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.ast.AstNode;

public class StringNode extends AstNode {
    private String value;

    public StringNode(String value) {
        this.value = value;
    }
}
