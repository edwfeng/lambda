package org.bergen.atcs.atics.lambda;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public interface Expression {
    /**
     * Runs the expression and returns the result.
     * @return result of the expression
     */
    default Expression run(Deque<Expression> stack, Map<Expression, Expression> replacements) {
        return replacements.getOrDefault(this, this);
    }

    default Expression run() {
        return run(new ArrayDeque<>(), new HashMap<>());
    }

    /**
     * Replaces all instances of {@code search} with {@code replaceWith} in the expression.
     *
     * @param search expression to search for
     * @param replaceWith expression to replace {@code search} with
     */
    default void replace(Expression search, Expression replaceWith) {}

    /**
     * Creates a new Expression that is η-equivalent to the Expression it is called on.
     *
     * @return new Expression
     */
    Expression deepCopy();

    default String expToString() {
        return expToString(new HashMap<>());
    }

    default String expToString(HashMap<Lambda.BoundVariable, String> map) {
        return null;
    }
}
