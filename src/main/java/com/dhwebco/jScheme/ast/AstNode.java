package com.dhwebco.jScheme.ast;

import java.util.ArrayList;
import java.util.List;

public class AstNode<T> {
    private T value;
    private List<AstNode> children = new ArrayList<>();

    public AstNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void addChild(AstNode child) {
        children.add(child);
    }
}
