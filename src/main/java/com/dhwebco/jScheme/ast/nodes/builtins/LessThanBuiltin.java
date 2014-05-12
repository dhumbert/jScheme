package com.dhwebco.jScheme.ast.nodes.builtins;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.values.BooleanValue;
import com.dhwebco.jScheme.values.IntegerValue;

public class LessThanBuiltin extends ArithmeticBuiltin {
    @Override
    public Value getValue() {
        IntegerValue operand1 = (IntegerValue)this.arguments.get(0);
        IntegerValue operand2 = (IntegerValue)this.arguments.get(1);

        return new BooleanValue(operand1.getValue() < operand2.getValue());
    }
}
