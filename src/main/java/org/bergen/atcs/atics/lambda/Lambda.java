package org.bergen.atcs.atics.lambda;

import java.util.function.Function;

public class Lambda implements Expression {
    public static class BoundVariable implements Expression {
        private BoundVariable() {}
    }

    public final BoundVariable parameter = new BoundVariable();
    public final Expression expression;

    public Lambda(Function<BoundVariable, Expression> makeExpression) {
        expression = makeExpression.apply(parameter);
    }
}
