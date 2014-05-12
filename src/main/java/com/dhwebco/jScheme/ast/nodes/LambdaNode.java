package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.NilValue;

public class LambdaNode extends CallableNode {
    @Override
    public Value getValue() {
        return new NilValue();
    }
}
