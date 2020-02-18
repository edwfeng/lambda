import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Lambda;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaTest {
    Lambda makeIdentityFunction() {
        return new Lambda(x -> x);
    }

    @Test
    void parameterIsUnique() {
        Lambda a = makeIdentityFunction();
        Lambda b = makeIdentityFunction();
        assertNotSame(a.parameter, b.parameter);
    }

    @Test
    void constructorInitializesExpressionWithFunction() {
        Expression expression = makeIdentityFunction();

        AtomicInteger numTimesCalled = new AtomicInteger(0);
        Lambda mLambda = new Lambda(boundVariable -> {
            numTimesCalled.incrementAndGet();
            return expression;
        });

        assertEquals(1, numTimesCalled.get());
        assertSame(expression, mLambda.getExpression());
    }

    @Test
    void constructorPassesBoundVariableToFunction() {
        Lambda mLambda = new Lambda(boundVariable -> boundVariable);
        assertSame(mLambda.parameter, mLambda.getExpression());
    }

    @Test
    void applyReplacesParameter() {
        Lambda lambda = makeIdentityFunction();
        Lambda ignoring = new Lambda(x -> lambda);

        assertSame(ignoring, lambda.apply(ignoring));
    }

    @Test
    void applyReplacesParameterRecursively() {
        Lambda ident = makeIdentityFunction();
        Lambda lambda = new Lambda(x -> new Lambda(y -> x));

        Expression applied = lambda.apply(ident);

        assertTrue(applied instanceof Lambda);
        assertSame(ident, ((Lambda) applied).getExpression());
    }

    @Test
    void deepCopyCreatesNewLambda() {
        Lambda lambda = makeIdentityFunction();
        Lambda newLambda = lambda.deepCopy();
        assertNotSame(lambda, newLambda);
        assertNotSame(lambda.parameter, newLambda.parameter);
        assertNotSame(lambda.getExpression(), newLambda.getExpression());

        // Check that they are both the identity function
        assertSame(lambda.parameter, lambda.getExpression());
        assertSame(newLambda.parameter, newLambda.getExpression());
    }

    @Test
    void deepCopyDeepCopiesLambdaInnerExpression() {
        Lambda lambda = new Lambda(x -> makeIdentityFunction());
        Lambda newLambda = lambda.deepCopy();
        assertNotSame(lambda, newLambda);
        assertNotSame(lambda.parameter, newLambda.parameter);
        assertNotSame(lambda.getExpression(), newLambda.getExpression());
    }
}
