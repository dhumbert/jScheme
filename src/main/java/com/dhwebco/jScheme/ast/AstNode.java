package com.dhwebco.jScheme.ast;

import java.util.ArrayList;
import java.util.List;

public class AstNode {
    private List<AstNode> children = new ArrayList<>();

    public void addChild(AstNode child) {
        children.add(child);
    }
}
