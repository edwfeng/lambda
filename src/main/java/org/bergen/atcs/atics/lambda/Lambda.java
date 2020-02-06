package org.bergen.atcs.atics.lambda;

import java.util.function.Function;

public class Lambda implements Expression {
    public static class BoundVariable implements Expression {
        private BoundVariable() {}

        public BoundVariable deepCopy() {return this;}
    }

    public final BoundVariable parameter;
    private Expression expression;

    public Lambda(Function<BoundVariable, Expression> makeExpression) {
        parameter = new BoundVariable();
        expression = makeExpression.apply(parameter);
    }

    public Expression getExpression() {
        return expression;
    }

    private void setExpression(Expression expression) {
        this.expression = expression;
    }

    /* public Expression apply(Expression arg) {
        return expression.replace(parameter, arg);
    } */

    @Override
    public void replace(Expression search, Expression replaceWith) {
        if (getExpression().equals(search)) {
            setExpression(replaceWith);
        } else {
            getExpression().replace(search, replaceWith);
        }
    }

    public Lambda deepCopy() {
        Expression newExpression = expression.deepCopy();
        return new Lambda(newParameter -> {
            newExpression.replace(parameter, newParameter);
            return newExpression;
        });
    }
}
