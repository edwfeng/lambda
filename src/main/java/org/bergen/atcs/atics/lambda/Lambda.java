package org.bergen.atcs.atics.lambda;

import java.util.function.Function;

public class Lambda implements Expression {
    public final Function<Expression, Expression> makeExpression;

    public Lambda(Function<Expression, Expression> makeExpression) {
        this.makeExpression = makeExpression;
    }

    public Expression apply(Expression arg) {
        return makeExpression.apply(arg);
    }
}
