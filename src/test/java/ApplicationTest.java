import org.bergen.atcs.atics.lambda.Application;
import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Lambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ApplicationTest {
    Lambda makeIdentityFunction() {
        return new Lambda(x -> x);
    }

    @Test
    void constructorCreatesFinalApplication() {
        Expression left = makeIdentityFunction();
        Expression right = makeIdentityFunction();
        Application application = new Application(left, right);
        assertSame(left, application.getLeft());
        assertSame(right, application.getRight());
    }

    @Test
    void deepCopyCreatesNewApplication() {
        Expression left = makeIdentityFunction();
        Expression right = makeIdentityFunction();
        Application application = new Application(left, right);
        Application newApplication = application.deepCopy();

        assertNotSame(application, newApplication);
        assertNotSame(left, newApplication.getLeft());
        assertNotSame(right, newApplication.getRight());
    }
}
