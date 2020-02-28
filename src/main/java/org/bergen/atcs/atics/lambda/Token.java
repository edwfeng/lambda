package org.bergen.atcs.atics.lambda;

import java.util.HashMap;

/**
 * A representation of an Expression.
 */
public class Token {
    /**
     * Type of token.
     */
    public final Type type;

    /**
     * Token name.
     */
    public final String name;

    private Token parent;
    private Token[] children;
    private int childStackPos;

    /**
     * Creates a new token.
     * @param type Type of token
     * @param name Token name
     */
    public Token(Type type, String name) {
        this.type = type;
        this.name = name;
        this.parent = null;
        this.children = new Token[type.getMaxChildren()];
        this.childStackPos = 0;
    }

    /**
     * Creates a token with an empty name.
     * @param type Type of token
     */
    public Token(Type type) {
        this(type, "");
    }

    /**
     * Returns the parent, if one exists. Else, null.
     * @return Parent of token or null
     */
    public Token getParent() {
        return parent;
    }

    /**
     * Sets the parent for the token.
     * @param parent Parent of token
     */
    public void setParent(Token parent) {
        this.parent = parent;
    }

    /**
     * Adds a child to the token.
     * @param child Child to add to token
     */
    public void addChild(Token child) {
        children[childStackPos++] = child;
    }

    /**
     * Removes a child from the token.
     */
    public void popChild() {
        children[--childStackPos] = null;
    }

    /**
     * Gets the number of children the token has.
     * @return Number of children
     */
    public int getNumChildren() {
        return childStackPos;
    }

    /**
     * Creates an equivalent Token
     * @return Copied Token
     */
    public Token copy() {
        return new Token(type, name);
    }

    /**
     * Converts Token to the represented Expression.
     * @return Represented Expression
     */
    public Expression convert() {
        return convert(new HashMap<>(), new HashMap<>());
    }

    /**
     * Coverts Token to the represented Expression, with a list of known BoundVariable names and FreeVariable expressions.
     * @param boundVars Map of names associated with BoundVariables
     * @param freeVars Stored values associated with free variable names
     * @return Represented Expression
     */
    public Expression convert(HashMap<String, Lambda.BoundVariable> boundVars, HashMap<String, Expression> freeVars) {
        switch (type) {
            case LAMBDA:
                return new Lambda(name -> {
                    @SuppressWarnings("unchecked") HashMap<String, Lambda.BoundVariable> newVars = (HashMap<String, Lambda.BoundVariable>) boundVars.clone();
                    newVars.put(this.name, name);
                    return children[0].convert(newVars, freeVars);
                });
            case APPLICATION:
                return new Application(children[0].convert(boundVars, freeVars), children[1].convert(boundVars, freeVars));
            case VARIABLE:
                if (boundVars.containsKey(this.name))
                    return boundVars.get(this.name);
                else if (freeVars.containsKey(this.name))
                    return freeVars.get(this.name);
                else
                    return new FreeVariable(this.name);
            default:
                return null;
        }
    }

    /**
     * Types of tokens.
     */
    public enum Type {
        PARENS_OPEN, PARENS_CLOSE, VARIABLE, LAMBDA(1), APPLICATION(2);

        /**
         * The maximum number of children the token can have.
         */
        private final int numChildren;

        Type(int numChildren) {
            this.numChildren = numChildren;
        }

        Type() {
            this.numChildren = 0;
        }

        /**
         * Gets number of children token can have.
         * @return Maximum number of children
         */
        public int getMaxChildren() {
            return this.numChildren;
        }
    }
}
