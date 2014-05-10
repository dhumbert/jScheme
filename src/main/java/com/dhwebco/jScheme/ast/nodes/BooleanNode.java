package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.BooleanValue;

public class BooleanNode extends AstNode {
    private boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }

    @Override
    public Value getValue() {
        return new BooleanValue(value);
    }
}
