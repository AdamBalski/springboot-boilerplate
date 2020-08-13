package pl.adambalski.springbootboilerplate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidatorTest {
    static final Validator<Object, ValidationResultImpl> returnsSuccess = o -> ValidationResultImpl.SUCCESS;
    static final Validator<Object, ValidationResultImpl> returnsFailure = o -> ValidationResultImpl.FAILURE;

    @Test
    void testReturnsSuccessAndReturnsFailure() {
        Validator<Object, ValidationResultImpl> validator = returnsSuccess.and(returnsFailure);
        ValidationResultImpl ValidationResultImpl = validator.validate(new Object());

        Assertions.assertEquals(pl.adambalski.springbootboilerplate.validation.ValidationResultImpl.FAILURE, ValidationResultImpl);
    }

    @Test
    void testReturnsSuccessAndReturnsSuccess() {
        Validator<Object, ValidationResultImpl> validator = returnsSuccess.and(returnsSuccess);
        ValidationResultImpl ValidationResultImpl = validator.validate(new Object());

        Assertions.assertEquals(pl.adambalski.springbootboilerplate.validation.ValidationResultImpl.SUCCESS, ValidationResultImpl);
    }

    @Test
    void testReturnsFailureAndReturnsFailure() {
        Validator<Object, ValidationResultImpl> validator = returnsFailure.and(returnsFailure);
        ValidationResultImpl ValidationResultImpl = validator.validate(new Object());

        Assertions.assertEquals(pl.adambalski.springbootboilerplate.validation.ValidationResultImpl.FAILURE, ValidationResultImpl);
    }

    @Test
    void testReturnFailureAndReturnsSuccess() {
        Validator<Object, ValidationResultImpl> validator = returnsFailure.and(returnsSuccess);
        ValidationResultImpl ValidationResultImpl = validator.validate(new Object());

        Assertions.assertEquals(pl.adambalski.springbootboilerplate.validation.ValidationResultImpl.FAILURE, ValidationResultImpl);
    }


}

enum ValidationResultImpl implements ValidationResult {
    SUCCESS, FAILURE;

    @Override
    public boolean isSuccess() {
        return this == SUCCESS;
    }
}