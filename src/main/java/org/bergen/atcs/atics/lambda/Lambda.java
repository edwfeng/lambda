package org.bergen.atcs.atics.lambda;

import java.util.function.Function;

public class Lambda implements Expression {
    public static class BoundVariable implements Expression {
        private BoundVariable() {}

        public BoundVariable deepCopy() {
            return this;
        }
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

    public Expression apply(Expression arg) {
        // If the lambda is just the identity function, return the argument immediately.
        if (parameter.equals(getExpression())) {
            return arg;
        }

        // Otherwise, copy the expression, replacing all instances of the bound variable
        // with arg.
        Expression newExpression = expression.deepCopy();
        newExpression.replace(parameter, arg);
        return newExpression;
    }

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
            if (parameter.equals(newExpression)) {
                return newParameter;
            }

            newExpression.replace(parameter, newParameter);
            return newExpression;
        });
    }
}
