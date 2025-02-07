import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.bergen.atcs.atics.lambda.Parser.*;

public class ParserTest {
    void printTokenList(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.type.equals(Token.Type.LAMBDA)) {
                System.out.printf("LAMBDA (%s)\n", token.name);
            } else if (token.type.equals(Token.Type.VARIABLE)) {
                System.out.printf("VARIABLE (%s)\n", token.name);
            } else {
                System.out.println(token.type);
            }
        }
    }

    @Test
    void parserParses() {
        printTokenList(parse("\\x.x"));
        System.out.println("================");
        printTokenList(parse("\\x.\\y.x"));
        System.out.println("================");
        printTokenList(parse("\\x.(\\y.x)"));
        System.out.println("================");
        printTokenList(parse("\\x.\\y.x(\\z.    z) yxyxyxyxyxxxyyyxxyy(x y)xyxyxxxyy(yy)"));
        System.out.println("================");
        printTokenList(parse("\\x.\\y.x(\\z.    z) yxyxyxyxyxx\\.xyyyxxyy(x y)xy\\xyxxxy(y(yy)"));
    }

    @Test
    void getNextWordLengthWorks() {
        assert (getCurrentWordLength("\\x.x", 1) == 1);
        assert (getCurrentWordLength("\\xy.x", 1) == 2);
        assert (getCurrentWordLength("\\x .x", 1) == 1);
        assert (getCurrentWordLength("\\x.x", 3) == 1);
        assert (getCurrentWordLength("\\x.xy", 3) == 2);
        assert (getCurrentWordLength("\\x.x ", 3) == 1);
    }

    @Test
    void doesTheThingIJustMadeWorkQuestionMark() {
        List<Token> tokens = parse("\\x.(a b) x");
        Token token = makeTree(tokens);
        Expression exp = token.convert();
        for (String name : exp.getFreeVariables(new ArrayList<>()))
            System.out.println(name);
        System.out.println(exp.expToString());
    }

    @Test
    void tryRunningSomethingForReal() {
        System.out.println(makeTree(parse("\\a.b c d")).convert().expToString());
        System.out.println(makeTree(parse("\\x.y\\z.b")).convert().expToString());
    }
}
