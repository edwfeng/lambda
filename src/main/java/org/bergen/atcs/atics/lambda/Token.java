package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Token {
    public enum Type {
        PARENS_OPEN, PARENS_CLOSE, VARIABLE, LAMBDA(1), APPLICATION(2);

        private final int numChildren;

        Type(int numChildren) {
            this.numChildren = numChildren;
        }
        Type() {
            this.numChildren = 0;
        }

        public int getMaxChildren() {
            return this.numChildren;
        }
    }

    public final String meta;
    public final Type type;
    private Token parent;
    private Token[] children;
    private int childStackPos;

    public Token(Type type, String meta) {
        this.type = type;
        this.meta = meta;
        this.parent = null;
        this.children = new Token[type.getMaxChildren()];
        this.childStackPos = 0;
    }

    public Token(Type type) {
        this.type = type;
        this.meta = "";
        this.parent = null;
        this.children = new Token[type.getMaxChildren()];
        this.childStackPos = 0;
    }

    public Token getParent() {
        return parent;
    }

    public void setParent(Token parent) {
        this.parent = parent;
    }

    public void addChild(Token child) {
        children[childStackPos++] = child;
    }

    public void clearChildren() {
        Arrays.fill(children, null);
        childStackPos = 0;
    }

    public Token[] getChildren() {
        return children;
    }

    public int getNumChildren() {
        return childStackPos;
    }

    public Token copy() {
        return new Token(type, meta);
    }

    public Expression convert() {
        return convert(new HashMap<>());
    }

    public Expression convert(HashMap<String, Lambda.BoundVariable> boundVariables) {
        switch (type) {
            case LAMBDA:
                return new Lambda(meta -> {
                    @SuppressWarnings("unchecked") HashMap<String, Lambda.BoundVariable> newVars = (HashMap<String, Lambda.BoundVariable>)boundVariables.clone();
                    newVars.put(this.meta, meta);
                    return children[0].convert(newVars);
                });
            case APPLICATION:
                return new Application(children[0].convert(boundVariables), children[1].convert(boundVariables));
            case VARIABLE:
                if (boundVariables.containsKey(this.meta))
                    return boundVariables.get(this.meta);
                else
                    return new FreeVariable(this.meta);
            default:
                return null;
        }
    }
}
