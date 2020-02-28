package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a free variable.
 */
public class FreeVariable implements Expression {
    /**
     * The name of the free variable.
     */
    public final String name;

    /**
     * Creates a new FreeVariable with the given name.
     * @param name Name of the FreeVariable
     */
    public FreeVariable(String name) {
        this.name = name;
    }

    public FreeVariable deepCopy() {
        return new FreeVariable(name);
    }

    public <TList extends List<String>> TList getFreeVariables(TList freeVars) {
        if (!freeVars.contains(name)) freeVars.add(name);
        return freeVars;
    }

    public String expToString(Map<Lambda.BoundVariable, String> map, List<String> freeVars) {
        return name;
    }
}
