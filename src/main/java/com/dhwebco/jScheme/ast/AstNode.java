package com.dhwebco.jScheme.ast;

import com.dhwebco.jScheme.Value;

import java.util.ArrayList;
import java.util.List;

abstract public class AstNode {
    private List<AstNode> children = new ArrayList<>();

    public void addChild(AstNode child) {
        children.add(child);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public List<AstNode> getChildren() {
        return this.children;
    }

    public AstNode getChild() {
        return this.children.get(0);
    }

    abstract public Value getValue();
}
