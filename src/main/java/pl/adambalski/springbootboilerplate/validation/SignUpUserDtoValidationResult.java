package pl.adambalski.springbootboilerplate.validation;

import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;

/**
 * Used as {@link SignUpUserDto} validation result<br><br>
 *
 * @see SignUpUserDto
 * @see SignUpUserDtoValidator
 * @see Validator
 * @author Adam Balski
 */
public enum SignUpUserDtoValidationResult implements ValidationResult {
    LOGIN_NOT_CORRECT,
    FULL_NAME_NOT_CORRECT,
    PASSWORD_NOT_CORRECT,
    PASSWORDS_DIFFERENT,
    SUCCESS;

    @Override
    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
