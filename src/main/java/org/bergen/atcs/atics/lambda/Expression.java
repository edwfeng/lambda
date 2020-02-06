package org.bergen.atcs.atics.lambda;

public interface Expression {
    /**
     * Runs the expression and returns the result.
     * @return result of the expression
     */
    default Expression run() {
        return this;
    }

    /**
     * Replaces all instances of {@code search} with {@code replaceWith} in the expression.
     *
     * @param search expression to search for
     * @param replaceWith expression to replace {@code search} with
     */
    default void replace(Expression search, Expression replaceWith) {}
}
