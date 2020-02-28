package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Expression {
    /**
     * Reduces the Expression and returns the result.
     *
     * @return the reduced expression, or null if reduction is not possible
     */
    default Expression reduce() {
        return null;
    }

    /**
     * <p>Attempts to reduce the Expression and returns the result.</p>
     *
     * <p>If unsuccessful, calls searchAndReduce() on each of its children from left to right until a reduction
     * succeeds.</p>
     *
     * <p>This has the effect of reducing the leftmost reducible Expression.</p>
     *
     * <p>Expressions containing other Expressions should override this default implementation.</p>
     *
     * @return the reduced expression, an Expression containing a reduced expression,
     * or null if reduction is not possible
     */
    default Expression searchAndReduce() {
        return reduce();
    }

    /**
     * Runs/simplifies the expression using normal order.
     *
     * @param expression expression to run/simplify
     * @return the simplified expression
     */
    static Expression run(Expression expression) {
        Expression previous = null;
        Expression current = expression;
        while (current != null) {
            previous = current;
            current = current.searchAndReduce();
        }

        return previous;
    }

    /**
     * Replaces all instances of {@code search} with {@code replaceWith} in the expression, in-place.
     *
     * @param search      expression to search for
     * @param replaceWith expression to replace {@code search} with
     */
    default void replace(Expression search, Expression replaceWith) {
    }

    /**
     * Creates a new Expression that is α-equivalent to the Expression it is called on.
     *
     * @return new α-equivalent expression
     */
    Expression deepCopy();

    /**
     * Searches the Expression for names of free variables and appends their names to a List.
     *
     * @param freeVars the list to add the variables to
     * @return freeVars, for convenience
     */
    default <TList extends List<String>> TList getFreeVariables(TList freeVars) {
        return freeVars;
    }

    /**
     * Determines whether two expressions are α-equivalent to each other.
     *
     * @param o Expression to compare to
     * @return true if o is α-equivalent to the Expression, false otherwise
     */
    default boolean equalsExp(Expression o) {
        if (o == null) return false;
        return this.expToString().equals(o.expToString());
    }

    /**
     * Exports the Expression to a string.
     * @return string representing the Expression
     */
    default String expToString() {
        return expToString(new HashMap<>(), getFreeVariables(new ArrayList<>()));
    }

    /**
     * Exports the Expression to a string, using given names for bound variables and free variables.
     * @param map names for bound variables
     * @param freeVars a list of names for free variables
     * @return string representing the Expression
     */
    default String expToString(Map<Lambda.BoundVariable, String> map, List<String> freeVars) {
        return null;
    }
}
