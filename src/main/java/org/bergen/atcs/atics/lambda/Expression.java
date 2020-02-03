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
     * Replaces all instances of {@code search} with {@code replaceWith} in the expression and returns the result.
     *
     * <p>To be compliant, all implementations must return {@code replaceWith} when {@code this.equals(search)} is {@code true}.</p>
     * @param search expression to search for
     * @param replaceWith expression to replace {@code search} with
     * @return a new expression where {@code search} is replaced with {@code replaceWith}.
     */
    default Expression replace(Expression search, Expression replaceWith) {
        // All implementations must contain this check to be compliant.
        if (equals(search)) {
            return replaceWith;
        }

        return this;
    }
}
