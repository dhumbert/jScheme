package com.dhwebco.jScheme.values;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;

import java.util.ArrayList;
import java.util.List;

public class ListValue extends Value {
    List<Value> values = new ArrayList<>();

    public ListValue(List<AstNode> values) {
        for (AstNode value : values) {
            this.values.add(value.getValue());
        }
    }

    @Override
    public List<Value> getValue() {
        return values;
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
