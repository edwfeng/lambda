package org.bergen.atcs.atics.lambda;

import java.util.ArrayList;
import java.util.HashMap;

public class FreeVariable implements Expression {
    public final String name;

    public FreeVariable(String name) {
        this.name = name;
    }

    public FreeVariable deepCopy() {
        return new FreeVariable(name);
    }

    public ArrayList<String> getFreeVariables(ArrayList<String> freeVars) {
        if (!freeVars.contains(name)) freeVars.add(name);
        return freeVars;
    }

    public String expToString(HashMap<Lambda.BoundVariable, String> map) {
        return name;
    }
}
