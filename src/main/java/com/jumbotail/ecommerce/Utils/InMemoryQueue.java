package com.jumbotail.ecommerce.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.stereotype.Component;

import com.jumbotail.ecommerce.Models.Event;



@Component
public class InMemoryQueue {
    private final Queue<Event> queue;

    public InMemoryQueue() {
        this.queue = new LinkedList<>();
    }

    public synchronized void enqueue(Event event) {
        queue.add(event);
        // Log statement to indicate that the event is enqueued
        System.out.println("Event enqueued: " + event.toString());
    }

    public synchronized Event dequeue() {
        return queue.poll();
    }

    public List<Event> dequeueBatch(int batchSize) {
        List<Event> batch = new ArrayList<>();

        // Dequeue events up to the specified batchSize or the current size of the queue
        int size = Math.min(batchSize, queue.size());
        for (int i = 0; i < size; i++) {
            batch.add(queue.poll());
        }

        return batch;
    }

    public List<Event> getQueueContents() {
        return new ArrayList<>(queue);
    }
}
