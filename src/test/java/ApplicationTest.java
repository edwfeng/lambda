import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Lambda;
import org.bergen.atcs.atics.lambda.Application;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {
    Lambda makeIdentityFunction() {
        return new Lambda(x -> x);
    }

    @Test void constructorCreatesFinalApplication() {
        Expression left = makeIdentityFunction();
        Expression right = makeIdentityFunction();
        Application application = new Application(left, right);
        assertSame(left, application.left);
        assertSame(right, application.right);
    }
}
