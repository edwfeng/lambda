package org.bergen.atcs.atics.lambda;

import java.util.Arrays;

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
}
