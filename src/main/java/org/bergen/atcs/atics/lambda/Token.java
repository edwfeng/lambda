package org.bergen.atcs.atics.lambda;

import java.util.HashMap;

public class Token {
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
        this(type, "");
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

    public void popChild() {
        children[--childStackPos] = null;
    }

    public int getNumChildren() {
        return childStackPos;
    }

    public Token copy() {
        return new Token(type, meta);
    }

    public Expression convert() {
        return convert(new HashMap<>(), new HashMap<>());
    }

    public Expression convert(HashMap<String, Lambda.BoundVariable> boundVars, HashMap<String, Expression> freeVars) {
        switch (type) {
            case LAMBDA:
                return new Lambda(meta -> {
                    @SuppressWarnings("unchecked") HashMap<String, Lambda.BoundVariable> newVars = (HashMap<String, Lambda.BoundVariable>) boundVars.clone();
                    newVars.put(this.meta, meta);
                    return children[0].convert(newVars, freeVars);
                });
            case APPLICATION:
                return new Application(children[0].convert(boundVars, freeVars), children[1].convert(boundVars, freeVars));
            case VARIABLE:
                if (boundVars.containsKey(this.meta))
                    return boundVars.get(this.meta);
                else if (freeVars.containsKey(this.meta))
                    return freeVars.get(this.meta);
                else
                    return new FreeVariable(this.meta);
            default:
                return null;
        }
    }

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
}
