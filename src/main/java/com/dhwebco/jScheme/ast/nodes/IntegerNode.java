package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.IntegerValue;

public class IntegerNode extends AstNode {
    private Integer value;

    public IntegerNode(Integer value) {
        this.value = value;
    }

    @Override
    public Value getValue() {
        return new IntegerValue(value);
    }
}
