package pl.adambalski.springbootboilerplate.wrappers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WrapperTest {
    @Test
    void testIntegerWrapper() {
        Wrapper<Integer> wrapper = new Wrapper<>(10);
        incrementIntegerWrapper(wrapper);

        assertEquals(10 + 1, wrapper.getValue());
    }

    @Test
    void testStringWrapper() {
        Wrapper<String> wrapper = new Wrapper<>("");
        nullifyContentOfStringWrapper(wrapper);

        assertNull(wrapper.getValue());
    }

    private void nullifyContentOfStringWrapper(Wrapper<String> wrapper) {
        wrapper.setValue(null);
    }

    private void incrementIntegerWrapper(Wrapper<Integer> wrapper) {
        wrapper.setValue(wrapper.getValue() + 1);
    }
}