package com.sellit.sellit.validation;

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
