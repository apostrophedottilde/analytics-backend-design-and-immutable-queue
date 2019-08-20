package com.gerwyn.jones.data.structures.immutable.queue;

public interface Queue<T> {
    Queue<T> enQueue(T t);
    Queue<T> deQueue();
    T head();
    boolean isEmpty();
}
