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

    @Test void applyCallsFunctionWithArg() {
        AtomicInteger numTimesCalled = new AtomicInteger(0);
        Lambda lambda = new Lambda(x -> {
            numTimesCalled.incrementAndGet();
            return x;
        });
        Lambda ignoring = new Lambda(x -> lambda);

        assertSame(ignoring, lambda.apply(ignoring));
    }
}
