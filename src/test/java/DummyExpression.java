import org.bergen.atcs.atics.lambda.Expression;

public class DummyExpression implements Expression {
    @Override
    public Expression deepCopy() {
        return this;
    }
}
