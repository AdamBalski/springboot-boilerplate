package pl.adambalski.springbootboilerplate.validation;


import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates {@link SignUpUserDto} if format is correct<br><br>
 *
 * login:<br>
 *  * 5 or more chars<br>
 *  * letters, numbers, +-, SHIFT + number<br>
 *  * example: UserName123<br><br>
 *
 *  fullName:<br>
 *  * Not empty, so minimum one name<br>
 *  * Capital format (first char of each word is uppercase and is follower by lowercase letters)<br>
 *  * only letters<br>
 *  * example: User Name<br><br><br>
 *
 *  password:<br>
 *  * both are equal to each other
 *  * format is similar to login's format, but minimum 8 chars
 */
public class SignUpUserDtoValidator {
    private static final Pattern loginPattern = Pattern.compile("[a-zA-Z0-9-+!@#$%^&*()]{5,}");
    private static final Pattern namePattern = Pattern.compile("[A-Z][a-z]*");
    private static final Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9-+!@#$%^&*()]{8,}");

    public static SignUpUserDtoValidationResult validate(SignUpUserDto signUpUserDto) {
        return SignUpUserDtoValidator.fullValidation.validate(signUpUserDto);
    }

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> loginValidation =
            signUpUserDto -> {
                String login = signUpUserDto.getLogin();
                Matcher matcher = loginPattern.matcher(login);
                return matcher.matches() ?
                        SignUpUserDtoValidationResult.SUCCESS :
                        SignUpUserDtoValidationResult.LOGIN_NOT_CORRECT;
            };

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> fullNameValidation =
            signUpUserDto -> {
                String fullName = signUpUserDto.getFullName();

                // Check if empty
                if(fullName.length() == 0) {
                    return SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT;
                }

                // Check all names for correctness
                String[] names = fullName.split(" ");
                for(String name : names) {
                    if(!namePattern.matcher(name).matches()) {
                        return SignUpUserDtoValidationResult.FULL_NAME_NOT_CORRECT;
                    }
                }

                return SignUpUserDtoValidationResult.SUCCESS;
            };

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> differentPasswordsValidation =
            dto -> Objects.equals(dto.getPassword1(), dto.getPassword2()) ?
                    SignUpUserDtoValidationResult.SUCCESS :
                    SignUpUserDtoValidationResult.PASSWORDS_DIFFERENT;

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> passwordValidation =
            signUpUserDto -> {
                String password = signUpUserDto.getPassword1();
                Matcher matcher = passwordPattern.matcher(password);
                return matcher.matches() ?
                        SignUpUserDtoValidationResult.SUCCESS :
                        SignUpUserDtoValidationResult.PASSWORD_NOT_CORRECT;
            };

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> fullValidation =
            dto -> loginValidation
                    .and(fullNameValidation)
                    .and(differentPasswordsValidation)
                    .and(passwordValidation)
                    .validate(dto);
}
