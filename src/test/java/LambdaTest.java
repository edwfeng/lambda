import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Lambda;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaTest {
    Lambda makeIdentityFunction() {
        return new Lambda(x -> x);
    }

    @Test void runReturnsItself() {
        Lambda lambda = makeIdentityFunction();
        assertSame(lambda, lambda.run());
    }

    @Test void parameterIsUnique() {
        Lambda a = makeIdentityFunction();
        Lambda b = makeIdentityFunction();
        assertNotSame(a.parameter, b.parameter);
    }

    @Test void constructorInitializesExpressionWithFunction() {
        Expression expression = makeIdentityFunction();

        AtomicInteger numTimesCalled = new AtomicInteger(0);
        @SuppressWarnings("NonAsciiCharacters") Lambda mλ = new Lambda(boundVariable -> {
            numTimesCalled.incrementAndGet();
            return expression;
        });

        assertEquals(1, numTimesCalled.get());
        assertSame(expression, mλ.expression);
    }

    @Test void constructorPassesBoundVariableToFunction() {
        Lambda mLambda = new Lambda(boundVariable -> boundVariable);
        assertSame(mLambda.parameter, mLambda.expression);
    }
}
