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

    enum Type {
        PARENS_OPEN, PARENS_CLOSE, VARIABLE, LAMBDA, APPLICATION
    }

    final String meta;
    final Type type;
}
