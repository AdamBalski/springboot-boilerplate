package pl.adambalski.springbootboilerplate.wrappers;

public class Pair<F, S> {
    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
