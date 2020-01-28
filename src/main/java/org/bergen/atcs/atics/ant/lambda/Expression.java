package main.java.org.bergen.atcs.atics.ant.lambda;

public interface Expression {
    default Expression run() {
        return this;
    }
}
