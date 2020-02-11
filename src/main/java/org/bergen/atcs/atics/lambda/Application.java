package org.bergen.atcs.atics.lambda;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Application implements Expression {
    private Expression left;
    private Expression right;

    public Application(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    private void setLeft(Expression left) {
        this.left = left;
    }

    private void setRight(Expression right) {
        this.right = right;
    }

    public Expression run(Deque<Expression> stack, Map<Expression, Expression> replacements) {
        if (replacements.containsKey(this)) {
            return replacements.get(this);
        }

        stack.push(right);
        int oldSize = stack.size();
        Expression result = left.run(stack, replacements);
        if (stack.size() < oldSize) {
            return result;
        } else {
            stack.pop();
            return new Application(result, right.run(stack, replacements));
        }
    }

    @Override
    public void replace(Expression search, Expression replaceWith) {
        if (getLeft().equals(search)) {
            setLeft(replaceWith);
        } else {
            getLeft().replace(search, replaceWith);
        }

        if (getRight().equals(search)) {
            setRight(replaceWith);
        } else {
            getRight().replace(search, replaceWith);
        }
    }

    public Application deepCopy() {
        return new Application(
                left.deepCopy(),
                right.deepCopy()
        );
    }

    public String expToString(HashMap<Lambda.BoundVariable, String> map) {
        return "(" + left.expToString(map) + " " + right.expToString(map) + ")";
    }
}
