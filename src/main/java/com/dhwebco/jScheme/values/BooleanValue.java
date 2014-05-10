package com.dhwebco.jScheme.values;

import com.dhwebco.jScheme.Value;

public class BooleanValue extends Value {
    private boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value) {
            return "#t";
        } else {
            return "#f";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o.getClass() == getClass()) {
            BooleanValue that = (BooleanValue) o;
            if (value != that.value) return false;
        } else if (o.getClass() == Boolean.class) {
            if (value != (Boolean) o) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }
}
