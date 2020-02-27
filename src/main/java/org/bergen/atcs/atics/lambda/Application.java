package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Application implements Expression {
    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    private void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    private void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public Expression reduce() {
        if (left instanceof Lambda) {
            return ((Lambda)left).apply(right);
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

        result = left.searchAndReduce();
        if (result != null) {
            return new Application(result, right);
        }

        result = right.searchAndReduce();
        if (result != null) {
            return new Application(left, result);
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

    public Application deepCopy() {
        return new Application(
                left.deepCopy(),
                right.deepCopy()
        );
    }

    public ArrayList<String> getFreeVariables(ArrayList<String> freeVars) {
        left.getFreeVariables(freeVars);
        right.getFreeVariables(freeVars);
        return freeVars;
    }

    public String expToString(HashMap<Lambda.BoundVariable, String> map, ArrayList<String> freeVars) {
        return "(" + left.expToString(map, freeVars) + " " + right.expToString(map, freeVars) + ")";
    }
}
