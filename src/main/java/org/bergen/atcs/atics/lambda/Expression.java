package org.bergen.atcs.atics.lambda;

public interface Expression {
    default Expression run() {
        return this;
    }

    default Expression replace(Expression search, Expression replaceWith) {
        // All implementations must contain this check to be compliant.
        if (equals(search)) {
            return replaceWith;
        }

        return this;
    }
}
