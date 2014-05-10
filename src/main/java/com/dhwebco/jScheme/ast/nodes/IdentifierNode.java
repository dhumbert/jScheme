package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.NilValue;
import com.dhwebco.jScheme.values.StringValue;

public class IdentifierNode extends AstNode {
    private String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    @Override
    public Value getValue() {
        return new StringValue(name);
    }
}
