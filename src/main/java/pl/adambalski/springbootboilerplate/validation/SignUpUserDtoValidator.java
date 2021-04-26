package pl.adambalski.springbootboilerplate.validation;


import pl.adambalski.springbootboilerplate.dto.SignUpUserDto;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates {@link SignUpUserDto} if format is correct<br><br>
 *
 * login:<br>
 *  * 5 <= number of characters <= 30<br>
 *  * letters, numbers, +-, SHIFT + number<br>
 *  * example: UserName123<br><br>
 *
 *  fullName:<br>
 *  * Ar most 50 characters<br>
 *  * Not empty, so minimum one name<br>
 *  * Capital format (first char of each word is uppercase and is follower by lowercase letters)<br>
 *  * only letters<br>
 *  * example: User Name<br><br>
 *
 *  email:<br>
 *  * At most 320 characters<br>
 *  * example: user@domain.png<br><br>
 *
 *  password:<br>
 *  * both passwords are equal to each other
 *  * format is similar to login's format, but minimum 8 chars<br><br>
 *
 * @see Validator
 * @see SignUpUserDtoValidationResult
 * @author Adam Balski
 */
public class SignUpUserDtoValidator {
    private static final Pattern loginPattern = Pattern.compile("[a-zA-Z0-9-+!@#$%^&*()]{5,30}"); // test
    private static final Pattern namePattern = Pattern.compile("[A-Z][a-z]{1,50}"); //
    private static final Pattern emailPattern = Pattern.compile(".{0,320}"); //
    private static final Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9-+!@#$%^&*()]{8,}");

    public static SignUpUserDtoValidationResult validate(SignUpUserDto signUpUserDto) {
        return SignUpUserDtoValidator.fullValidation.validate(signUpUserDto);
    }

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> loginValidation =
            signUpUserDto -> {
                String login = signUpUserDto.login();
                Matcher matcher = loginPattern.matcher(login);
                return matcher.matches() ?
                        SignUpUserDtoValidationResult.SUCCESS :
                        SignUpUserDtoValidationResult.LOGIN_NOT_CORRECT;
            };

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> fullNameValidation =
            signUpUserDto -> {
                String fullName = signUpUserDto.fullName();

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
            dto -> Objects.equals(dto.password1(), dto.password2()) ?
                    SignUpUserDtoValidationResult.SUCCESS :
                    SignUpUserDtoValidationResult.PASSWORDS_DIFFERENT;

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> emailValidation =
            signUpUserDto -> {
                String email = signUpUserDto.email();
                Matcher matcher = emailPattern.matcher(email);
                return matcher.matches() ?
                        SignUpUserDtoValidationResult.SUCCESS :
                        SignUpUserDtoValidationResult.EMAIL_NOT_CORRECT;
            };

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> passwordValidation =
            signUpUserDto -> {
                String password = signUpUserDto.password1();
                Matcher matcher = passwordPattern.matcher(password);
                return matcher.matches() ?
                        SignUpUserDtoValidationResult.SUCCESS :
                        SignUpUserDtoValidationResult.PASSWORD_NOT_CORRECT;
            };

    private static final Validator<SignUpUserDto, SignUpUserDtoValidationResult> fullValidation =
            dto -> loginValidation
                    .and(fullNameValidation)
                    .and(differentPasswordsValidation)
                    .and(emailValidation)
                    .and(passwordValidation)
                    .validate(dto);
}
