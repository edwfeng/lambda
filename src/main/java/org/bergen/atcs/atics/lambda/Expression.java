package org.bergen.atcs.atics.lambda;

public interface Expression {
    default Expression run() {
        return this;
    }
}
