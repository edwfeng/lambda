package org.bergen.atcs.atics.lambda;

public class Application implements Expression {
    public final Expression left;
    public final Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
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

    public Application deepCopy() {
        return new Application(
                left.deepCopy(),
                right.deepCopy()
        );
    }
}
