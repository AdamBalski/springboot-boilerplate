package pl.adambalski.springbootboilerplate.wrappers;

/**
 * Wraps class into itself.<br>
 * For example may be use to make Integer or int "mutable"
 * <pre>{@code
 * void increment(Wrapper<Integer> integer) {
 *     integer.value++;
 * }
 * }</pre>
 * After executing increment, value would be incremented.
 * If 'integer' would be pure {@code int} or {@link Integer},
 * then after incrementing the value would remain the same.<br><br>
 * @param <C> wrapped class
 * @author Adam Balski
 */
public class Wrapper<C> {
    private C value;

    public Wrapper(C value) {
        this.value = value;
    }

    public C getValue() {
        return value;
    }

    public void setValue(C value) {
        this.value = value;
    }
}
