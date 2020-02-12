import static org.bergen.atcs.atics.lambda.Parser.*;

import org.bergen.atcs.atics.lambda.Expression;
import org.bergen.atcs.atics.lambda.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserTest {
    void printTokenList(List<Token> tokens) {
        for (Token token : tokens) {
            if (token.type.equals(Token.Type.LAMBDA)) {
                System.out.printf("LAMBDA (%s)\n", token.meta);
            } else if (token.type.equals(Token.Type.VARIABLE)) {
                System.out.printf("VARIABLE (%s)\n", token.meta);
            } else {
                System.out.println(token.type);
            }
        }
    }

    @Test void parserParses() {
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

    @Test void getNextWordLengthWorks() {
        assert(getCurrentWordLength("\\x.x", 1) == 1);
        assert(getCurrentWordLength("\\xy.x", 1) == 2);
        assert(getCurrentWordLength("\\x .x", 1) == 1);
        assert(getCurrentWordLength("\\x.x", 3) == 1);
        assert(getCurrentWordLength("\\x.xy", 3) == 2);
        assert(getCurrentWordLength("\\x.x ", 3) == 1);
    }

    @Test void doesTheThingIJustMadeWorkQuestionMark() {
        List<Token> tokens = parse("\\x.(a b) x");
        Token token = makeTree(tokens);
        Expression exp = token.convert();
        System.out.println(exp.expToString());
    }
}
