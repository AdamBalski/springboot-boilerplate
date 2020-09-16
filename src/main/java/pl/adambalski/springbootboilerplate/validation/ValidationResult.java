package pl.adambalski.springbootboilerplate.validation;

/**
 * Enums implementing this class are used in {@link Validator}<br><br>
 *
 * @see Validator
 * @author Adam Balski
 */
public interface ValidationResult {
    boolean isSuccess();
}