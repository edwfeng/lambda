package org.bergen.atcs.atics.lambda;

public class FreeVariable implements Expression {
    public final String name;

    public FreeVariable(String name) {
        this.name = name;
    }
}
