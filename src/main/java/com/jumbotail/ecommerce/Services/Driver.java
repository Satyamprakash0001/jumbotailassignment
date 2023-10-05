package com.jumbotail.ecommerce.Services;

import java.util.List;

import com.jumbotail.ecommerce.Models.Event;
import com.jumbotail.ecommerce.Models.User;



public class Driver implements Runnable {
    private final User user;
    private final EventService eventService;

    public Driver(User user, EventService eventService) {
        this.user = user;
        this.eventService = eventService;
    }

    @Override
    public void run() {
        int i=0;
        while (i<=20) {
            long startTime = System.currentTimeMillis();
            eventService.logApiResponseTime(startTime);

            // Generate and enqueue events
            List<Event> eventBatch = eventService.batchGenerateEvents(user, 5); // Adjust batch size as needed
            eventService.invokeWebhook(eventBatch);

            // Simulate a delay between events
            try {
                Thread.sleep((int) (Math.random() * 5000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
