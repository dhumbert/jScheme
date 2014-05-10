package com.dhwebco.jScheme.values;

import com.dhwebco.jScheme.Value;

public class IntegerValue extends Value {
    int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o.getClass() == getClass()) {
            IntegerValue that = (IntegerValue) o;
            if (value != that.value) return false;
        } else if (o.getClass() == Integer.class) {
            if (value != (Integer) o) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
