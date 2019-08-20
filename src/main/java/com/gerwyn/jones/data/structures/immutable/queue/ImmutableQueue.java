package com.gerwyn.jones.data.structures.immutable.queue;

import java.util.LinkedList;
import java.util.List;

public class ImmutableQueue<T> implements Queue<T> {

    private List<T> queueState;

    private boolean isEmpty;

    public ImmutableQueue() {
        queueState = new LinkedList<T>();
        isEmpty = true;
    }

    public ImmutableQueue(ImmutableQueue queue) {
        queueState = queue.queueState;
        isEmpty = queue.isEmpty;
    }

    private ImmutableQueue(List<T> queueState) {
        this.queueState = queueState;
    }

    @Override
    public Queue<T> enQueue(T t) {
        List<T> newQ = new LinkedList<T>(this.queueState);
        newQ.add(t);
        isEmpty = false;
        return new ImmutableQueue<T>(newQ);
    }

    @Override
    public Queue<T> deQueue() {
        List<T> newQ = new LinkedList<T>(this.queueState);
        if (queueState.size() == 1)
            return new ImmutableQueue<T>();
        newQ.remove(0);
        return new ImmutableQueue<T>(newQ);
    }

    @Override
    public T head() {
        if (queueState.size() == 0)
            return null;
        return queueState.get(0);
    }

    @Override
    public boolean isEmpty() {
        // we can save cpu cycles by tracking this locally so we
        // dont need to invoke isEmpty()
        return isEmpty;
    }

    @Override
    public String toString() {
        return "ImmutableQueue{" +
                "queueState=" + queueState +
                '}';
    }
}
