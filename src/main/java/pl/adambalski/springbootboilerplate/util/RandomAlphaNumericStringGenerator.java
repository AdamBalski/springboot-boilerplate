package pl.adambalski.springbootboilerplate.util;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;

/**
 * Is used to create random strings of specified length, which contain numbers and letters (both uppercase and lowercase). <br><br>
 *
 * @author Adam Balski
 * @see SecurityConfiguration
 * @see RandomString
 */
@Component
public class RandomAlphaNumericStringGenerator {
    private final int length;

    public RandomAlphaNumericStringGenerator() {
        this(SecurityConfiguration.REFRESH_TOKEN_LENGTH);
    }

    public RandomAlphaNumericStringGenerator(int length) {
        this.length = length;
    }

    public String generate(int length) {
        return new RandomString(length).nextString();
    }

    public String generate() {
        return generate(12);
    }
}
