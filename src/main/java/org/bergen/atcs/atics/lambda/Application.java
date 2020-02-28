package org.bergen.atcs.atics.lambda;

import java.util.List;
import java.util.Map;

/**
 * Represents an application of one Expression onto another.
 */
public class Application implements Expression {
    private Expression left;
    private Expression right;

    /**
     * Constructs an Application with two expressions.
     * @param left Expression on the left
     * @param right Expression on the right
     */
    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the Expression on the left.
     * @return Expression on the left
     */
    public Expression getLeft() {
        return left;
    }

    private void setLeft(Expression left) {
        this.left = left;
    }

    /**
     * Gets the Expression on the right.
     * @return Expression on the right
     */
    public Expression getRight() {
        return right;
    }

    private void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public Expression reduce() {
        if (getLeft() instanceof Lambda) {
            return ((Lambda)getLeft()).apply(getRight());
        } else {
            return null;
        }
    }

    @Override
    public Expression searchAndReduce() {
        Expression result;

        result = reduce();
        if (result != null) {
            return result;
        }

        result = getLeft().searchAndReduce();
        if (result != null) {
            return new Application(result, getRight());
        }

        result = getRight().searchAndReduce();
        if (result != null) {
            return new Application(getLeft(), result);
        }

        return null;
    }

    @Override
    public void replace(Expression search, Expression replaceWith) {
        if (getLeft().equals(search)) {
            setLeft(replaceWith);
        } else {
            getLeft().replace(search, replaceWith);
        }

        if (getRight().equals(search)) {
            setRight(replaceWith);
        } else {
            getRight().replace(search, replaceWith);
        }
    }

    @Override
    public Application deepCopy() {
        return new Application(
                getLeft().deepCopy(),
                getRight().deepCopy()
        );
    }

    @Override
    public <TList extends List<String>> TList getFreeVariables(TList freeVars) {
        getLeft().getFreeVariables(freeVars);
        getRight().getFreeVariables(freeVars);
        return freeVars;
    }

    @Override
    public String expToString(Map<Lambda.BoundVariable, String> map, List<String> freeVars) {
        return "(" + getLeft().expToString(map, freeVars) + " " + getRight().expToString(map, freeVars) + ")";
    }
}
