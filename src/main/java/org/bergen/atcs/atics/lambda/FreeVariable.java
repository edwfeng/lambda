package org.bergen.atcs.atics.lambda;

import java.util.HashMap;

public class FreeVariable implements Expression {
    public final String name;

    public FreeVariable(String name) {
        this.name = name;
    }

    public FreeVariable deepCopy() {
        return new FreeVariable(name);
    }

    public String expToString(HashMap<Lambda.BoundVariable, String> map) {
        return name;
    }
}
