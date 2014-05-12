package com.dhwebco.jScheme.ast.nodes.builtins;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.ast.nodes.CallableNode;
import com.dhwebco.jScheme.values.IntegerValue;

import java.util.List;

public class ArithmeticBuiltin extends CallableNode {
    @Override
    protected boolean checkNumberOfArguments(int number) {
        return number == 2;
    }

    @Override
    protected boolean checkTypeOfArguments(List<Value> args) {
        return args.get(0).getClass() == IntegerValue.class
                && args.get(1).getClass() == IntegerValue.class;
    }
}
