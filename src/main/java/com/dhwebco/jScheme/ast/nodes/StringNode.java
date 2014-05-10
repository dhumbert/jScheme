package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.StringValue;

public class StringNode extends AstNode {
    private String value;

    public StringNode(String value) {
        this.value = value;
    }

    @Override
    public Value getValue() {
        return new StringValue(value);
    }
}
