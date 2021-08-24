package pl.adambalski.springbootboilerplate.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import pl.adambalski.springbootboilerplate.security.SecurityConfiguration;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class RandomAlphaNumericStringGeneratorTest {
    @Test
    void testGenerateWithDefaultLength() {
        String randomString = "ABC123456789";

        var mock = Mockito.mock(RandomAlphaNumericStringGenerator.class);
        when(mock.generate())
                .thenCallRealMethod();
        when(mock.generate(12))
                .thenReturn(randomString);

        ReflectionTestUtils.setField(mock, "length", SecurityConfiguration.REFRESH_TOKEN_LENGTH);

        assertEquals(randomString, mock.generate());
    }

    @Test
    void testGenerateIsDistinctive() {
        RandomAlphaNumericStringGenerator generator = new RandomAlphaNumericStringGenerator();

        String[] stringArray = new String[10];

        for(int i = 0; i < stringArray.length; i++) {
            stringArray[i] = generator.generate(100);
        }

        assertEquals(10, Arrays.stream(stringArray).distinct().count());
    }

    @Test
    void testGenerateIsGivingResultsOfDesiredLength() {
        RandomAlphaNumericStringGenerator generator = new RandomAlphaNumericStringGenerator();

        String[] stringArray = new String[10];
        int length = 20;

        for(int i = 0; i < stringArray.length; i++) {
            stringArray[i] = generator.generate(length);
        }

        Predicate<String> isOfDesiredLength = string -> string.length() == length;
        assertTrue(Arrays.stream(stringArray).allMatch(isOfDesiredLength));
    }

    @Test
    void testGenerateIsGivingResultsWithCorrectCharacterSet() {
        RandomAlphaNumericStringGenerator generator = new RandomAlphaNumericStringGenerator();

        String characterSet = characterSet();

        Supplier<String> createRandomString = () -> generator.generate(20);
        Predicate<String> doesContainOnlyDesiredCharacters = string -> {
            for (char c : string.toCharArray()) {
                if(characterSet.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
        };

        assertTrue(
                Stream
                        .generate(createRandomString)
                        .limit(10)
                        .allMatch(doesContainOnlyDesiredCharacters)
        );
    }

    private String characterSet() {
        StringBuilder result = new StringBuilder();

        char character;
        for(character = '0'; character <= '9'; ++character) {
            result.append(character);
        }

        for(character = 'a'; character <= 'z'; ++character) {
            result.append(character);
        }

        for(character = 'A'; character <= 'Z'; ++character) {
            result.append(character);
        }

        return result.toString();
    }
}