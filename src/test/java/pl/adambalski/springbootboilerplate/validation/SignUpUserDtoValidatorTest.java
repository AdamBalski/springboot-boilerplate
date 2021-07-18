package pl.adambalski.springbootboilerplate.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;

import java.util.List;

class SignUpUserDtoValidatorTest {
    private List<SignUpUserDto> dtos;
    private SignUpUserDto correctDto;

    @BeforeEach
    void init() {
        correctDto = new SignUpUserDto(
                "username",
                "User Name",
                "user@name.jpg",
                "password",
                "password"
        );

        dtos = List.of(
                new SignUpUserDto( // Login is less than 5 chars
                        "user",
                        "User Name",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Login is too long
                        "useruseruseruseruseruseruseruseruseruseruseruseruser",
                        "User Name",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Login contains a forbidden chars ('?')
                        "username?",
                        "User Name",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // One of names in full name is lowercase
                        "username",
                        "user Name",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Full name is empty
                        "username",
                        "",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Full name is too long
                        "username",
                        "Full Name Full Name Full Name Full Name Full Name Full Name Full Name Full Name",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( //  Full name contains a forbidden char ('?')
                        "username",
                        "User Name?",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Password's length is less than 8
                        "username",
                        "User Name",
                        "user@name.jpg",
                        "pass",
                        "pass"
                ),
                new SignUpUserDto( // Password contains a forbidden char ('?')
                        "username",
                        "User Name",
                        "user@name.jpg",
                        "password?",
                        "password?"
                ),
                new SignUpUserDto( // Passwords are different
                        "username",
                        "User Name",
                        "user@name.jpg",
                        "password1",
                        "password2"
                ),
                new SignUpUserDto( // Email is too long
                        "username",
                        "User Name",
                        "user@name.jpg".repeat(100),
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Username is null
                        null,
                        "User Name",
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Full name is null
                        "username",
                        null,
                        "user@name.jpg",
                        "password",
                        "password"
                ),
                new SignUpUserDto( // E-mail is null
                        "username",
                        "User Name",
                        null,
                        "password",
                        "password"
                ),
                new SignUpUserDto( // Password1 is null
                        "username",
                        "User Name",
                        "user@name.jpg",
                        null,
                        "password"
                ),
                new SignUpUserDto( // Password2 is null
                        "username",
                        "User Name",
                        "user@name.jpg",
                        "password",
                        null
                ),
                new SignUpUserDto( // Every field is null
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                new SignUpUserDto( // Login and password2 is null
                        null,
                        "User Name",
                        "user@name.jpg",
                        "password",
                        null
                ));
    }

    @Test
    void testValidationIfDtoIsCorrect() {
        Assertions.assertEquals(
                SignUpUserDtoValidationResult.SUCCESS,
                SignUpUserDtoValidator.validate(correctDto)
        );
    }

    @Test
    void testIfShortLogin() {
        SignUpUserDto userDto = dtos.get(0);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.LOGIN_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfTooLongLogin() {
        SignUpUserDto userDto = dtos.get(1);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.LOGIN_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfLoginContainsForbiddenChars() {
        SignUpUserDto userDto = dtos.get(2);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.LOGIN_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfFullNameNotCapital() {
        SignUpUserDto userDto = dtos.get(3);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfEmptyFullName() {
        SignUpUserDto userDto = dtos.get(4);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfTooLongFullName() {
        SignUpUserDto userDto = dtos.get(5);

        System.out.println(userDto.fullName());
        System.out.println(userDto.fullName().length());

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfFullNameContainsForbiddenChar() {
        SignUpUserDto userDto = dtos.get(6);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfShortPassword() {
        SignUpUserDto userDto = dtos.get(7);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.PASSWORD_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfPasswordContainsForbiddenChar() {
        SignUpUserDto userDto = dtos.get(8);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.PASSWORD_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfPasswordsAreDifferent() {
        SignUpUserDto userDto = dtos.get(9);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.PASSWORDS_DIFFERENT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfTooLongEmail() {
        SignUpUserDto userDto = dtos.get(10);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.EMAIL_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfLoginIsNull() {
        SignUpUserDto userDto = dtos.get(11);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfFullNameIsNull() {
        SignUpUserDto userDto = dtos.get(12);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfEmailIsNull() {
        SignUpUserDto userDto = dtos.get(13);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfPassword1IsNull() {
        SignUpUserDto userDto = dtos.get(14);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfPassword2IsNull() {
        SignUpUserDto userDto = dtos.get(15);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfEveryFieldIsNull() {
        SignUpUserDto userDto = dtos.get(16);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfLoginAndPassword2AreNull() {
        SignUpUserDto userDto = dtos.get(17);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.AT_LEAST_ONE_FIELD_IS_NULL,
                SignUpUserDtoValidator.validate(userDto)
        );
    }
}