package org.bergen.atcs.atics.lambda;

import java.util.*;
import java.util.function.Function;

public class Lambda implements Expression {
    /**
     * Bound variable representing the parameter for this lambda.
     */
    public final BoundVariable parameter;

    private Expression expression;

    /**
     * Constructs a lambda by calling makeExpression with a bound variable representing the parameter.
     * @param makeExpression function that given a BoundVariable, returns an Expression for the lambda
     */
    public Lambda(Function<BoundVariable, Expression> makeExpression) {
        parameter = new BoundVariable();
        expression = makeExpression.apply(parameter);
    }

    /**
     * Gets the expression of this Lambda.
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    private void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Expression searchAndReduce() {
        Expression reduceResult = reduce();
        if (reduceResult != null) {
            return reduceResult;
        }

        Expression searchResult = getExpression().searchAndReduce();
        if (searchResult != null) {
            return new Lambda(newParameter -> {
                if (parameter.equals(searchResult)) {
                    return newParameter;
                }

                searchResult.replace(parameter, newParameter);
                return searchResult;
            });
        }

        return null;
    }

    /**
     * Applies arg to the lambda and returns the result.
     * @param arg Expression to apply
     * @return result of applying arg to the Lambda
     */
    public Expression apply(Expression arg) {
        // If the lambda is just the identity function, return the argument immediately.
        if (parameter.equals(getExpression())) {
            return arg;
        }

        // Otherwise, copy the expression, replacing all instances of the bound variable
        // with arg.
        Expression newExpression = getExpression().deepCopy();
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

    @Override
    public Lambda deepCopy() {
        Expression newExpression = getExpression().deepCopy();
        return new Lambda(newParameter -> {
            if (parameter.equals(newExpression)) {
                return newParameter;
            }

            newExpression.replace(parameter, newParameter);
            return newExpression;
        });
    }

    @Override
    public <TList extends List<String>> TList getFreeVariables(TList freeVars) {
        getExpression().getFreeVariables(freeVars);
        return freeVars;
    }

    @Override
    public String expToString(Map<BoundVariable, String> map, List<String> freeVars) {
        return "(λ" + parameter.expToString(map, freeVars) + "." + getExpression().expToString(map, freeVars) + ")";
    }

    public static class BoundVariable implements Expression {
        // Don't let anyone construct this class.
        private BoundVariable() {}

        @Override
        public BoundVariable deepCopy() {
            return this;
        }

        private String getNextName(Map<BoundVariable, String> map, List<String> freeVars) {
            String result;

            if (map.size() == 0)
                result = "a";
            else {
                String curString = Collections.max(map.values(),
                        (str1, str2) -> {
                            if (str1.length() != str2.length())
                                return str1.length() - str2.length();
                            return str1.compareTo(str2);
                        });
                StringBuilder newString = new StringBuilder();

                boolean prevCarry = true;
                for (int i = curString.length() - 1; i >= 0; i--) {
                    char x = curString.charAt(i);
                    if (prevCarry)
                        newString.append((char) (((x + 1 - 'a') % ('z' - 'a' + 1)) + 'a'));
                    else
                        newString.append(x);

                    prevCarry &= x + 1 > 'z';
                }

                if (prevCarry)
                    newString.append("a");

                result = newString.reverse().toString();
            }

            if (freeVars.contains(result)) {
                map.put(new BoundVariable(), result);
                return getNextName(map, freeVars);
            }
            return result;
        }

        @Override
        public String expToString(Map<BoundVariable, String> map, List<String> freeVars) {
            if (!map.containsKey(this))
                map.put(this, getNextName(map, freeVars));

            return map.get(this);
        }
    }
}
