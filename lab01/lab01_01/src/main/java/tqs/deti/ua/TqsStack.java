package tqs.deti.ua;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TqsStack<T> {
    private LinkedList<T> stack;
    private int maxSize;
    private boolean isBounded;

    public TqsStack() {
        stack = new LinkedList<>();
        this.isBounded = false;
    }

    public TqsStack(int maxSize) {
        stack = new LinkedList<>();
        this.maxSize = maxSize;
        this.isBounded = true;
    }

    public T pop() {
        if (stack.isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return stack.pop();
    }

    public void push(T element) {
        if (isBounded && stack.size() >= maxSize) {
            throw new IllegalStateException("Stack is full");
        }
        stack.push(element);
    }

    public T peek() {
        if (stack.isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public T popTopN(int n) {
        if (n <= 0 || n > stack.size()) {
            throw new NoSuchElementException("Invalid number of elements to pop");
        }
        T top = null;
        for (int i = 0; i < n; i++) {
            top = stack.removeFirst();
        }
        return top;
    }
}
