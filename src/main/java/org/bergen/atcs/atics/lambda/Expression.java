package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.HashMap;

public interface Expression {
    /**
     * Runs the expression and returns the result.
     *
     * @return result of the expression
     */
    default Expression run() {
        return this;
    }

    /**
     * Replaces all instances of {@code search} with {@code replaceWith} in the expression.
     *
     * @param search      expression to search for
     * @param replaceWith expression to replace {@code search} with
     */
    default void replace(Expression search, Expression replaceWith) {
    }

    /**
     * Creates a new Expression that is η-equivalent to the Expression it is called on.
     *
     * @return new Expression
     */
    Expression deepCopy();

    default ArrayList<String> getFreeVariables(ArrayList<String> freeVars) {
        return freeVars;
    }

    default boolean equalsExp(Expression o) {
        if (o == null) return false;
        return this.expToString().equals(o.expToString());
    }

    default String expToString() {
        return expToString(new HashMap<>(), getFreeVariables(new ArrayList<>()));
    }

    default String expToString(HashMap<Lambda.BoundVariable, String> map, ArrayList<String> freeVars) {
        return null;
    }
}
