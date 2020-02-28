import org.bergen.atcs.atics.lambda.Application;
import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Lambda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests of common lambda expressions and evaluates
 * them using the {@see Lambda} and {@see Application} classes.
 * <p>
 * This class does not test the parser/output capabilities of the
 * lambda lab.
 */
public class LambdaApplicatorTest {
    Lambda trueLambda() {
        return new Lambda(x -> new Lambda(y -> x));
    }

    Lambda falseLambda() {
        return new Lambda(f -> new Lambda(x -> x));
    }

    Lambda notLambda() {
        return new Lambda(p -> new Application(new Application(p, falseLambda()), trueLambda()));
    }

    private Expression number(Expression f, Expression x, int depth) {
        return depth == 0 ? x : new Application(f, number(f, x, depth - 1));
    }

    Lambda number(int depth) {
        return new Lambda(f -> new Lambda(x -> number(f, x, depth)));
    }

    Lambda succ() {
        return new Lambda(n -> new Lambda(f -> new Lambda(x -> new Application(f, new Application(new Application(n, f), x)))));
    }

    Lambda pred() {
        return new Lambda(n -> new Lambda(f -> new Lambda(x -> new Application(new Application(new Application(n, new Lambda(g -> new Lambda(h -> new Application(h, new Application(g, f))))), new Lambda(u -> x)), new Lambda(u -> u)))));
    }

    Lambda subtract() {
        return new Lambda(m -> new Lambda(n -> new Application(new Application(n, pred()), m)));
    }

    Lambda times() {
        return new Lambda(n -> new Lambda(m -> new Lambda(f -> new Lambda(x -> new Application(new Application(n, new Application(m, f)), x)))));
    }

    Lambda y() {
        return new Lambda(f -> new Application(new Lambda(x -> new Application(f, new Application(x, x))), new Lambda(x -> new Application(f, new Application(x, x)))));
    }

    Lambda ifLambda() {
        return new Lambda(b -> new Lambda(T -> new Lambda(F -> new Application(new Application(b, T), F))));
    }

    Lambda isZero() {
        return new Lambda(n -> new Application(new Application(n, new Lambda(x -> falseLambda())), trueLambda()));
    }

    Lambda factorial() {
        return new Lambda(f -> new Lambda(n -> new Application(new Application(new Application(ifLambda(), new Application(isZero(), n)), number(1)), new Application(new Application(times(), n), new Application(f, new Application(new Application(subtract(), n), number(1)))))));
    }

    private static void assertNumber(Lambda lambda, int number) {
        DummyExpression f = new DummyExpression();
        DummyExpression x = new DummyExpression();

        Expression inner = Expression.run(lambda.apply(f));
        assertTrue(inner instanceof Lambda);

        Expression current = Expression.run(((Lambda)inner).apply(x));

        for (int i = 0; i < number; i++) {
            assertTrue(current instanceof Application);
            Application app = (Application)current;
            assertSame(f, app.getLeft());
            current = app.getRight();
        }

        assertSame(x, current);
    }

    private static boolean isTrueLambda(Expression expression) {
        DummyExpression x = new DummyExpression();
        DummyExpression y = new DummyExpression();

        Application application = new Application(new Application(expression, x), y);

        return x == Expression.run(application);
    }

    private static boolean isFalseLambda(Expression expression) {
        DummyExpression x = new DummyExpression();
        DummyExpression y = new DummyExpression();

        Application application = new Application(new Application(expression, x), y);

        return y == Expression.run(application);
    }

    @Test void trueLambdaWorks() {
        assertTrue(isTrueLambda(trueLambda()));
        assertFalse(isFalseLambda(trueLambda()));
    }

    @Test
    void falseLambdaWorks() {
        assertTrue(isFalseLambda(falseLambda()));
        assertFalse(isTrueLambda(falseLambda()));
    }

    @Test
    void notTrue() {
        Expression expression = Expression.run(new Application(notLambda(), trueLambda()));

        assertTrue(isFalseLambda(expression));
        assertFalse(isTrueLambda(expression));
    }

    @Test
    void notFalse() {
        Expression expression = Expression.run(new Application(notLambda(), falseLambda()));

        assertTrue(isTrueLambda(expression));
        assertFalse(isFalseLambda(expression));
    }

    @Test
    void numbersZeroToTen() {
        for (int i = 0; i <= 10; i++) {
            assertNumber(number(i), i);
        }
    }

    @Test
    void succZeroToTen() {
        Expression current = number(0);
        Lambda succ = succ();
        for (int i = 0; i <= 10; i++) {
            current = Expression.run(succ.apply(current));
            assert (current instanceof Lambda);
            assertNumber((Lambda) current, i + 1);
        }
    }

    @Test
    void ifTrue() {
        DummyExpression x = new DummyExpression();
        DummyExpression y = new DummyExpression();
        Application app = new Application(new Application(new Application(ifLambda(), trueLambda()), x), y);
        assertSame(x, Expression.run(app));
    }

    @Test
    void ifFalse() {
        DummyExpression x = new DummyExpression();
        DummyExpression y = new DummyExpression();
        Application app = new Application(new Application(new Application(ifLambda(), falseLambda()), x), y);
        assertSame(y, Expression.run(app));
    }

    @Test
    void ifZero() {
        DummyExpression x = new DummyExpression();
        DummyExpression y = new DummyExpression();
        Application app = new Application(new Application(new Application(ifLambda(), new Application(isZero(), number(0))), x), y);
        assertSame(x, Expression.run(app));
    }

    @Test
    void ifNotZero() {
        DummyExpression x = new DummyExpression();
        DummyExpression y = new DummyExpression();
        Application app = new Application(new Application(new Application(ifLambda(), new Application(isZero(), number(1))), x), y);
        assertSame(y, Expression.run(app));
    }

    @Test
    void subtractTest() {
        Application app = new Application(new Application(subtract(), number(3)), number(1));
        assertNumber((Lambda) Expression.run(app), 2);
    }

    @Test
    void timesTest() {
        Application app = new Application(new Application(times(), number(2)), number(3));
        assertNumber((Lambda) Expression.run(app), 6);
    }

    @Test
    void printFunctions() {
        System.out.println(trueLambda().expToString());
        System.out.println(falseLambda().expToString());
        System.out.println(notLambda().expToString());
        System.out.println(ifLambda().expToString());
        System.out.println(succ().expToString());
        System.out.println(pred().expToString());
        System.out.println(y().expToString());
    }

    @Test
    void factorialZeroToFive() {
        int current = 1;
        for (int i = 0; i <= 5; i++) {
            System.out.printf("Computing (Y factorial %d)\n", i);
            if (i > 0) current *= i;
            Application app = new Application(new Application(y(), factorial()), number(i));
            Expression thing = Expression.run(app);
            assertNumber((Lambda) thing, current);
        }
    }
}
