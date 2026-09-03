package csse3030.week7;

import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * A plain, deterministic test used to drive Daikon's Chicory front end
 * (Part 3) - unlike the jqwik properties elsewhere in this project, Daikon
 * needs a fixed, repeatable set of calls to trace, not freshly generated
 * inputs on every run.
 *
 * <p>Mirrors the testCaseSingleValues / testCaseRandom pattern from
 * Ammann &amp; Offutt, <i>Introduction to Software Testing</i>, Ch. 19.
 *
 * <p>Also carries a {@code main} method: Chicory instruments a target the
 * same way {@code java <class>} would, so this class needs a plain entry
 * point in addition to its {@code @Test} methods.
 */
public class BoundedStackTraceTest {

    @Test
    void testCaseSingleValues() {
        BoundedStack<Integer> stack = new BoundedStack<>(5);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        stack.push(4);
        stack.push(5);
        stack.peek();
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
    }

    @Test
    void testCaseRandom() {
        Random random = new Random(42);
        int[] capacities = {1, 2, 3, 5, 8, 10, 15};
        for (int capacity : capacities) {
            BoundedStack<Integer> stack = new BoundedStack<>(capacity);
            int pushed = 0;
            for (int step = 0; step < capacity * 3; step++) {
                boolean canPush = pushed < capacity;
                boolean canPop = pushed > 0;
                boolean doPush = canPush && (!canPop || random.nextBoolean());
                if (doPush) {
                    stack.push(random.nextInt(1000));
                    pushed++;
                } else if (canPop) {
                    stack.pop();
                    pushed--;
                }
            }
        }
    }

    public static void main(String[] args) {
        BoundedStackTraceTest test = new BoundedStackTraceTest();
        test.testCaseSingleValues();
        test.testCaseRandom();
    }
}
