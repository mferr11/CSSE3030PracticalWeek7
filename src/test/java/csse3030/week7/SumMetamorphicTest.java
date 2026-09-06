package csse3030.week7;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Warm-up for Part 4 (metamorphic testing): a metamorphic relation on a
 * plain list of integers, independent of ProductSearch. Mirrors the
 * lecture's own permutation example directly.
 */
class SumMetamorphicTest {

    // TODO (practical Part 4 warm-up): add a @Property method here
    // implementing the lecture's permutation example directly: for any
    // list of integers xs, permuting it must not change its sum.

    private static int sum(List<Integer> xs) {
        return xs.stream().mapToInt(Integer::intValue).sum();
    }
}
