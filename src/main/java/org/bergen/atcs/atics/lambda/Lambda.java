package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class Lambda implements Expression {
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

    public ArrayList<String> getFreeVariables(ArrayList<String> freeVars) {
        expression.getFreeVariables(freeVars);
        return freeVars;
    }

    public String expToString(HashMap<BoundVariable, String> map, ArrayList<String> freeVars) {
        return "(λ" + parameter.expToString(map, freeVars) + "." + expression.expToString(map, freeVars) + ")";
    }

    public static class BoundVariable implements Expression {
        private BoundVariable() {
        }

        public BoundVariable deepCopy() {
            return this;
        }

        private String getNextName(HashMap<BoundVariable, String> map, ArrayList<String> freeVars) {
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
        public String expToString(HashMap<BoundVariable, String> map, ArrayList<String> freeVars) {
            if (!map.containsKey(this))
                map.put(this, getNextName(map, freeVars));

            return map.get(this);
        }
    }
}
