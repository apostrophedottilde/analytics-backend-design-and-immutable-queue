package com.gerwyn.jones.data.structures.immutable.queue;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ImmutableQueueTest {

    @Test
    public void shouldReturnNewQueueWithAppendedElementWhenEnqueue() {
        Queue<Integer> q1 = new ImmutableQueue<Integer>();
        Queue<Integer> q2 = q1.enQueue(1);
        assertNotEquals(q1, q2);
        assertEquals(1, q2.head().intValue());
    }

    @Test
    public void shouldRemoveMostRecentlyAddedElementAndReturnNewQueueWhenDequeue() {
        Queue<Integer> q1 = new ImmutableQueue<Integer>();
        q1 = q1.enQueue(1);
        assertEquals(1, q1.head().intValue());
        q1 = q1.deQueue();
        assertEquals(q1.head(), null);
    }

    @Test
    public void newQueueShouldHaveZeroElements() {
        assertTrue(new ImmutableQueue<>().isEmpty());
    }

    @Test
    public void shouldReturnNullWhenHeadIsNotPresent() {
        Queue<String> q1 = new ImmutableQueue<>();
        assertEquals(q1.head(), null);
    }

    @Test
    public void shouldEvaluateWhetherQueueIsEmpty() {
        Queue<String> q1 = new ImmutableQueue<>();
        q1 = q1.enQueue("new entry 1");
        assertEquals(q1.isEmpty(), false);
        q1 = q1.deQueue();
        assertEquals(q1.isEmpty(), true);
    }
}
