package pl.adambalski.springbootboilerplate.wrappers;

/**
 * Wraps class into itself.<br><br>
 * For example may be used to make {@link Integer} "mutable".<br>
 *
 * First example:
 * <pre>
 * void increment(Integer integer) {
 *     integer++;
 * }
 * </pre>
 * Second example:
 * <pre>
 * void increment(Wrapper<Integer> integer) {
 *     integer.setValue(integer.getValue() + 1);
 * }
 * </pre>
 *
 * If we were to increment a usual {@link Integer} like in the first example,
 * then a reference to that {@link Integer} would change,
 * because {@link Integer} is immutable.<br><br>
 *
 * A reference to a {@link Wrapper} in the second example wouldn't change, because the {@link Wrapper} is mutable,
 * and a reference to integer is inside of the wrapper.<br><br>
 *
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
