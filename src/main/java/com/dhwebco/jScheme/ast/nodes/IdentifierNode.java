package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.ast.AstNode;

public class IdentifierNode extends AstNode {
    private String name;

    public IdentifierNode(String name) {
        this.name = name;
    }
}
