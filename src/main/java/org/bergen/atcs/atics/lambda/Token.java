package org.bergen.atcs.atics.lambda;

public class Token {
    public Token(Type type, String meta) {
        this.type = type;
        this.meta = meta;
    }

    public Token(Type type) {
        this.type = type;
        this.meta = "";
    }

    public enum Type {
        PARENS_OPEN, PARENS_CLOSE, VARIABLE, LAMBDA, APPLICATION
    }

    public final String meta;
    public final Type type;
}
