package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FreeVariable implements Expression {
    public final String name;

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
