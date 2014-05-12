package com.dhwebco.jScheme.ast.nodes;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.AstNode;
import com.dhwebco.jScheme.values.NilValue;

import java.util.ArrayList;
import java.util.List;

public class CallableNode extends AstNode {
    protected List<Value> arguments;

    public void setArguments(List<Value> args) throws Exception {
        if (checkNumberOfArguments(args.size())) {
            if (checkTypeOfArguments(args)) {
                this.arguments = args;
            } else {
                throw new Exception("Invalid argument type");
            }
        } else {
            throw new Exception("Invalid number of arguments.");
        }
    }

    @Override
    public Value getValue() {
        return new NilValue();
    }

    protected boolean checkNumberOfArguments(int number) {
        return number == 0;
    }

    protected boolean checkTypeOfArguments(List<Value> args) {
        return true;
    }
}
