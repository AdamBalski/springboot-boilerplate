package pl.adambalski.springbootboilerplate.wrappers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PairTest {
    @Test
    void testIncrementingPairOfIntegers() {
        Pair<Integer, Integer> pair = new Pair<>(0, 0);
        incrementFieldsOfPair(pair);

        assertAll(List.of(
                () -> assertEquals(1, pair.getFirst()),
                () -> assertEquals(1, pair.getSecond())
        ));
    }

    private void incrementFieldsOfPair(Pair<Integer, Integer> pair) {
        pair.setFirst(pair.getFirst() + 1);
        pair.setSecond(pair.getSecond() + 1);
    }
}