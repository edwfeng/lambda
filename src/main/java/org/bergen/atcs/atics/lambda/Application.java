package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.HashMap;

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

    public Expression getRight() {
        return right;
    }

    private void setLeft(Expression left) {
        this.left = left;
    }

    private void setRight(Expression right) {
        this.right = right;
    }

    public Expression run() {
        Expression leftResolved = left.run();
        if (leftResolved instanceof Lambda) {
            return ((Lambda) leftResolved).apply(right).run();
        } else {
            return new Application(leftResolved, right.run());
        }
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

    public String expToString(HashMap<Lambda.BoundVariable, String> map) {
        return "(" + left.expToString(map) + " " + right.expToString(map) + ")";
    }
}
