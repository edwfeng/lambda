package org.bergen.atcs.atics.lambda;

import java.util.function.Function;

public class Lambda implements Expression {
    // BoundVariable is no longer used when evaluating expressions.
    // It will be moved to the lambda serializer, which will use
    // the class as a placeholder for the true value of the bound
    // variable.

    public final Function<Expression, Expression> makeExpression;

    public Lambda(Function<Expression, Expression> makeExpression) {
        this.makeExpression = makeExpression;
    }

    public Expression apply(Expression arg) {
        return makeExpression.apply(arg);
    }
}
