package pl.adambalski.springbootboilerplate.validation;

/**
 * Implementations of a {@link Validator}, return a value from a corresponding implementation of
 * {@link ValidationResult} enum.<br><br>
 *
 * @see Validator
 * @author Adam Balski
 */
public interface ValidationResult {
    boolean isSuccess();
}