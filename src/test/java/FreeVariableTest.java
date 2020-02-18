import org.bergen.atcs.atics.lambda.FreeVariable;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class FreeVariableTest {
    @Test
    void constructorConstructsWithName() {
        String name = "free";
        FreeVariable freeVar = new FreeVariable(name);
        assertEquals(name, freeVar.name);
    }

    @Test
    void nameIsFinal() throws NoSuchFieldException {
        assertTrue(Modifier.isFinal(FreeVariable.class.getField("name").getModifiers()));
    }

    @Test
    void runReturnsItself() {
        String name = "free";
        FreeVariable freeVar = new FreeVariable(name);
        assertSame(freeVar, freeVar.run());
        assertEquals(name, freeVar.name);
    }

    @Test
    void deepCopyCreatesNewFreeVariable() {
        String name = "free";
        FreeVariable freeVar = new FreeVariable(name);
        FreeVariable newFreeVar = freeVar.deepCopy();

        assertNotSame(freeVar, newFreeVar);
        assertEquals(name, newFreeVar.name);
    }
}
