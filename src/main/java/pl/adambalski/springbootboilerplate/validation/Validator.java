package pl.adambalski.springbootboilerplate.validation;

/**
 * Performs validation.
 * {@link #and(Validator)} method allows to chain different validators together.<br><br>
 *
 * @param <T> class to validate
 * @param <R> enum containing possible validation results (extends @link {@link ValidationResult})
 *
 * @see ValidationResult
 * @author Adam Balski
 */
@FunctionalInterface
public interface Validator<T, R extends ValidationResult> {
    R validate(T t);

    default Validator<T, R> and(Validator<T, R> validator) {
        return dto -> {
            R r = this.validate(dto);
            return r.isSuccess() ? validator.validate(dto) : r;
        };
    }
}