import static org.bergen.atcs.atics.lambda.Parser.getCurrentWordLength;

import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test void getNextWordLengthWorks() {
        assert(getCurrentWordLength("\\x.x", 1) == 1);
        assert(getCurrentWordLength("\\xy.x", 1) == 2);
        assert(getCurrentWordLength("\\x .x", 1) == 1);
        assert(getCurrentWordLength("\\x.x", 3) == 1);
        assert(getCurrentWordLength("\\x.xy", 3) == 2);
        assert(getCurrentWordLength("\\x.x ", 3) == 1);
    }
}
