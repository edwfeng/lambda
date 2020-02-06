package org.bergen.atcs.atics.lambda;

import java.util.function.Function;

public class Lambda implements Expression {
    public static class BoundVariable implements Expression {
        private BoundVariable() {}
    }

    public final BoundVariable parameter;
    public final Expression expression;

    public Lambda(Function<BoundVariable, Expression> makeExpression) {
        parameter = new BoundVariable();
        expression = makeExpression.apply(parameter);
    }

    /* public Expression apply(Expression arg) {
        return expression.replace(parameter, arg);
    } */

    public void replace(Expression search, Expression replaceWith) {

    }
}
