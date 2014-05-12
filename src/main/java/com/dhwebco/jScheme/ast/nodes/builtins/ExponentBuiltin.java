package com.dhwebco.jScheme.ast.nodes.builtins;

import com.dhwebco.jScheme.Value;
import com.dhwebco.jScheme.values.IntegerValue;

public class ExponentBuiltin extends ArithmeticBuiltin {
    @Override
    public Value getValue() {
        IntegerValue operand1 = (IntegerValue)this.arguments.get(0);
        IntegerValue operand2 = (IntegerValue)this.arguments.get(1);

        return new IntegerValue(((Double)Math.pow(operand1.getValue(), operand2.getValue())).intValue());
    }
}
