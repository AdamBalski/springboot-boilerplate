package com.sellit.sellit.validation;

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