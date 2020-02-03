package org.bergen.atcs.atics.lambda;

public class Application implements Expression {
    public final Expression left;
    public final Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression replace(Expression search, Expression replaceWith) {
        if (equals(search)) {
            return replaceWith;
        }

        return new Application(
                left.replace(search, replaceWith),
                right.replace(search, replaceWith)
        );
    }
}
