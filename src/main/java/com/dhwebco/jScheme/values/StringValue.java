package com.dhwebco.jScheme.values;

import com.dhwebco.jScheme.Value;

public class StringValue extends Value {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (getClass() == o.getClass()) {
            StringValue that = (StringValue) o;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;
        }

        if (o.getClass() == String.class) {
            if (!value.equals(o)) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
