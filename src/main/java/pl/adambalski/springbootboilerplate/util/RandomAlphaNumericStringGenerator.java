package pl.adambalski.springbootboilerplate.util;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

@Component
public class RandomAlphaNumericStringGenerator {
    public String generate(int length) {
        return new RandomString(length).nextString();
    }

    public String generate() {
        return generate(12);
    }
}
