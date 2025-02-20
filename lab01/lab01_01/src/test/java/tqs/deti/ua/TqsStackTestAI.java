package tqs.deti.ua;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TqsStackTestAI {
    private TqsStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new TqsStack<>();
    }

    @Test
    void testPushAndPop() {
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
    }

    @Test
    void testPopOnEmptyStackThrowsException() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @Test
    void testPeek() {
        stack.push(5);
        assertEquals(5, stack.peek());
        assertEquals(1, stack.size()); // Ensure peek does not remove element
    }

    @Test
    void testPeekOnEmptyStackThrowsException() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }

    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, stack.size());
        stack.push(10);
        stack.push(20);
        assertEquals(2, stack.size());
    }

    @Test
    void testBoundedStack() {
        TqsStack<Integer> boundedStack = new TqsStack<>(2);
        boundedStack.push(1);
        boundedStack.push(2);
        assertThrows(IllegalStateException.class, () -> boundedStack.push(3));
    }
}
