package org.bergen.atcs.atics.antedw.lambda;

public interface Expression {
    default Expression run() {
        return this;
    }
}
