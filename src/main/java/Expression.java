public interface Expression {
    default Expression run() {
        return this;
    }
}
