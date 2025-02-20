package tqs.deti.ua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TqsStackTest {
    // log  not working
    private static final Logger log = LoggerFactory.getLogger(TqsStackTest.class);

    private final int MAX_SIZE = 10;

    TqsStack<String> tqsStack;
    TqsStack<String> tbsLimitedStack;


    @BeforeEach
    void setup() {
        tqsStack = new TqsStack<>();
        tbsLimitedStack = new TqsStack<>(MAX_SIZE);
    }

    @Test
    void emptyOnConstruction() {
        log.debug("Testing whether the stack is empty on construction.");
        // exercise

        // verify
        Assertions.assertTrue(tqsStack.isEmpty());
    }

    @Test
    void stackSizeOnConstruction() {
        log.debug("Testing whether the stack has a size of 0 on construction.");
        // exercise

        // verify
        Assertions.assertEquals(0, tqsStack.size());
    }

    @Test void stackNotEmptyAfterPush() {
        int n = new Random().nextInt(100);
        log.debug("Testing whether the stack is empty and has size of {} after {} pushes.",n,n);
        // exercise
        for (int i = 0; i < n; i++) {
            tqsStack.push("Element " + i);
        }
        // verify
        Assertions.assertEquals(tqsStack.size(), n);
        Assertions.assertFalse(tqsStack.isEmpty());
    }

    @Test void pushXreturnsXPoop() {
        String x = "Element";
        log.debug("Testing whether the stack returns the x value \"{}\" and maintains the previous size, on pop after x being pushed to the stack.",x);
        // exercise
        int previusSize = tqsStack.size();
        tqsStack.push(x);
        // verify
        Assertions.assertEquals(x, tqsStack.pop());
        Assertions.assertEquals(previusSize, tqsStack.size());
    }

    @Test void pushXreturnsXPeek() {
        String x = "Element";
        log.debug("Testing whether the stack returns the x value \"{}\" but maintains the new size, on peek after x being pushed to the stack.",x);
        // exercise
        tqsStack.push(x);
        int size = tqsStack.size();
        // verify
        Assertions.assertEquals(x, tqsStack.peek());
        Assertions.assertEquals(size, tqsStack.size());
    }

    @Test void stackEmptyAfterNPops() {
        int n = new Random().nextInt(100);
        log.debug("Testing whether the stack is empty and has size of 0 after {} pops.",n);
        // exercise
        for (int i = 0; i < n; i++) {
            tqsStack.push("Element " + i);
        }
        for (int i = 0; i < n; i++) {
            tqsStack.pop();
        }
        // verify
        Assertions.assertEquals(0, tqsStack.size());
        Assertions.assertTrue(tqsStack.isEmpty());
    }

    @Test void popEmptyStackThrowsException() {
        log.debug("Testing whether the stack throws a NoSuchElementException when popping from an empty stack.");
        // exercise
        // verify
        Assertions.assertThrows(java.util.NoSuchElementException.class, tqsStack::pop);
    }

    @Test void peekEmptyStackThrowsException() {
        log.debug("Testing whether the stack throws a NoSuchElementException when peeking from an empty stack.");
        // exercise
        // verify
        Assertions.assertThrows(java.util.NoSuchElementException.class, tqsStack::peek);
    }

    @Test void pushToFullStackThrowsException() {
        log.debug("Testing whether the stack throws an IllegalStateException when pushing to a full stack.");
        // exercise
        for (int i = 0; i < MAX_SIZE; i++) {
            tbsLimitedStack.push("Element " + i);
        }
        // verify
        Assertions.assertThrows(IllegalStateException.class, () -> tbsLimitedStack.push("Element"));
    }

    @Test
    void testPopTopN() {
        log.debug("Testing popTopN method.");

        // exercise
        tqsStack.push("1");
        tqsStack.push("2");
        tqsStack.push("3");
        tqsStack.push("4");
        tqsStack.push("5");

        String result = tqsStack.popTopN(3);

        // verify

        Assertions.assertEquals("3", result);
        Assertions.assertEquals(2, tqsStack.size());
   }
}



