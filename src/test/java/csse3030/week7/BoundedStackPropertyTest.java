package csse3030.week7;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Property-based tests for {@link BoundedStack}, checking the two contracts
 * the class documents rather than any single hand-picked example:
 *
 * <ul>
 *   <li>the invariant {@code 0 <= size() <= capacity()} always holds, and</li>
 *   <li>popping more times than were pushed is a contract violation.</li>
 * </ul>
 */
class BoundedStackPropertyTest {

    @Property
    void sizeNeverExceedsCapacity(@ForAll @IntRange(min = 1, max = 50) int capacity) {
        BoundedStack<Integer> stack = new BoundedStack<>(capacity);
        for (int i = 0; i < capacity; i++) {
            stack.push(i);
        }
        assertTrue(stack.isFull(), "stack should report full once size == capacity");

        // Pushing one more than capacity must be rejected, not silently accepted.
        assertThrows(IllegalStateException.class, () -> stack.push(-1));
        assertTrue(
                stack.size() <= stack.capacity(),
                () -> "invariant violated: size=" + stack.size() + " > capacity=" + stack.capacity());
    }

    @Property
    void poppingAnEmptyStackThrows(@ForAll @IntRange(min = 1, max = 50) int capacity) {
        BoundedStack<Integer> stack = new BoundedStack<>(capacity);
        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @Property
    void poppingMoreThanWasPushedThrows(
            @ForAll @IntRange(min = 1, max = 50) int capacity,
            @ForAll @IntRange(min = 0, max = 50) int pushCount) {
        int actualPushCount = Math.min(capacity, pushCount);
        BoundedStack<Integer> stack = new BoundedStack<>(capacity);
        for (int i = 0; i < actualPushCount; i++) {
            stack.push(i);
        }
        for (int i = 0; i < actualPushCount; i++) {
            stack.pop();
        }
        assertThrows(NoSuchElementException.class, stack::pop);
    }
}
