package pl.adambalski.springbootboilerplate.util;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;

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
