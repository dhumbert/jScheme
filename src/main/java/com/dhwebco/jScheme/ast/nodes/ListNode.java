package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.ListValue;
import com.dhwebco.jScheme.values.NilValue;

import java.util.List;

public class ListNode extends AstNode {
    @Override
    public Value getValue() {
        return new ListValue(this.getChildren());
    }
}
