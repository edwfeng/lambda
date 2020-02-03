package org.bergen.atcs.atics.lambda;

public class Application implements Expression {
    public final Expression left;
    public final Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression run() {
        Expression leftResolved = left.run();
        if (leftResolved instanceof Lambda) {
            return ((Lambda) leftResolved).apply(right.run()).run();
        } else {
            return this;
        }
    }
}
