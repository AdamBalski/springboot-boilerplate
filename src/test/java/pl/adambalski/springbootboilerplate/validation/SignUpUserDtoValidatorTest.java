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
    void testIfLoginContainsForbiddenChars() {
        SignUpUserDto userDto = dtos.get(1);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.LOGIN_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfFullNameNotCapital() {
        SignUpUserDto userDto = dtos.get(2);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfEmptyFullName() {
        SignUpUserDto userDto = dtos.get(3);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfFullNameContainsForbiddenChar() {
        SignUpUserDto userDto = dtos.get(4);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfShortPassword() {
        SignUpUserDto userDto = dtos.get(5);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.PASSWORD_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfPasswordContainsForbiddenChar() {
        SignUpUserDto userDto = dtos.get(6);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.PASSWORD_NOT_CORRECT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

    @Test
    void testIfPasswordsAreDifferent() {
        SignUpUserDto userDto = dtos.get(7);

        Assertions.assertEquals(
                SignUpUserDtoValidationResult.PASSWORDS_DIFFERENT,
                SignUpUserDtoValidator.validate(userDto)
        );
    }

}