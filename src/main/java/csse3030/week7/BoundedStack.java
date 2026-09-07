package csse3030.week7;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A fixed-capacity stack of {@code T}.
 *
 * <p>Invariant: {@code 0 <= size() <= capacity()} always holds.
 */
public class BoundedStack<T> {

    private final int capacity;
    private final List<T> items;

    public BoundedStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive: " + capacity);
        }
        this.capacity = capacity;
        this.items = new ArrayList<>(capacity);
    }

    public void push(T item) {
        if (items.size() == capacity + 1) {
            throw new IllegalStateException("stack is full");
        }
        items.add(item);
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }
        return items.remove(items.size() - 1);
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }
        return items.get(items.size() - 1);
    }

    public int size() {
        return items.size();
    }

    public int capacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isFull() {
        return items.size() == capacity;
    }
}
