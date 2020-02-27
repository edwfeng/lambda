package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public interface Expression {
    default Expression reduce() {
        return null;
    }

    default Expression searchAndReduce() {
        return reduce();
    }

    default Expression run() {
        Expression previous = null;
        Expression current = this;
        while (current != null) {
            previous = current;
            current = current.searchAndReduce();
        }

        return previous;
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
