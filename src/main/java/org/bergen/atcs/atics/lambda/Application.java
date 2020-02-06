package org.bergen.atcs.atics.lambda;

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

    /* public Expression run() {
        Expression leftResolved = left.run();
        if (leftResolved instanceof Lambda) {
            return ((Lambda) leftResolved).apply(right.run()).run();
        } else {
            return this;
        }
    } */

    /* public Expression replace(Expression search, Expression replaceWith) {
        if (equals(search)) {
            return replaceWith;
        }

        return new Application(
                left.replace(search, replaceWith),
                right.replace(search, replaceWith)
        );
    } */
}
