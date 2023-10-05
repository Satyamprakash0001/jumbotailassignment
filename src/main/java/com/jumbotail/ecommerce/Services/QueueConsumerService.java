package com.jumbotail.ecommerce.Services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jumbotail.ecommerce.Models.Event;
import com.jumbotail.ecommerce.Models.User;
import com.jumbotail.ecommerce.Repositories.EventRepository;
import com.jumbotail.ecommerce.Repositories.UserRepository;
import com.jumbotail.ecommerce.Utils.InMemoryQueue;



@Service
public class QueueConsumerService {
    
    private final InMemoryQueue inMemoryQueue;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public QueueConsumerService(InMemoryQueue inMemoryQueue, EventRepository eventRepository, UserRepository userRepository) {
        this.inMemoryQueue = inMemoryQueue;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public void consumeEvents() {
        while (true) {
            try {
                // Dequeue events from the in-memory queue in batches
                List<Event> eventBatch = inMemoryQueue.dequeueBatch(10); // Adjust batch size as needed

                // Handle duplicate events
                filterDuplicateEvents(eventBatch);

                // Push events into the database with retry mechanism
                boolean success = pushEventsToDatabaseWithRetry(eventBatch, 3); // Retry 3 times

                if (success) {
                    // If events are successfully pushed, filter unique users and push them to the database
                    // List<User> uniqueUsers = filterUniqueUsers(eventBatch);
                    // pushUsersToDatabase(uniqueUsers, 3); // Retry 3 times
                } else {
                    // Log or handle the failure to push events
                    System.err.println("Failed to push events to the database after retrying");
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Implement the logic to filter duplicate events
    private void filterDuplicateEvents(List<Event> events) {
        // Add logic to filter duplicate events
        // You may want to use a HashSet or other data structures to track duplicates
        Set<String> uniqueEventIds = new HashSet<>();

        // Filter duplicate events based on event IDs
        events.removeIf(event -> !uniqueEventIds.add(event.getId()));
    }

    // Implement the logic to push events into the database with retry mechanism
    private boolean pushEventsToDatabaseWithRetry(List<Event> events, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                // Insert events into the database
                eventRepository.saveAll(events);
                return true; // Success
            } catch (Exception e) {
                // Log or handle the exception
                System.err.println("Error inserting events into the database. Retrying...");
                retryCount++;
            }
        }
        return false; // Failure after retries
    }

        // Implement the logic to push users into the database with retry mechanism
    public boolean pushUserToDatabase(User user, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                // Insert users into the database
                userRepository.save(user);
                return true; // Success
            } catch (Exception e) {
                // Log or handle the exception
                System.err.println("Error inserting users into the database. Retrying...");
                retryCount++;
            }
        }
        return false;
    }


}
