package org.bergen.atcs.atics.lambda;

public interface Expression {
    /**
     * Runs the expression and returns the result.
     * @return result of the expression
     */
    default Expression run() {
        return this;
    }
}
