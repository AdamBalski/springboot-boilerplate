package pl.adambalski.springbootboilerplate.wrappers;

public class Wrapper<C> {
    C c;

    public Wrapper(C c) {
        this.c = c;
    }

    public Wrapper() {
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }
}
