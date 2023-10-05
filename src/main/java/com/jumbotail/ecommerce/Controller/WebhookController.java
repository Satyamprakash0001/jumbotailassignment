package com.jumbotail.ecommerce.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jumbotail.ecommerce.Models.Event;
import com.jumbotail.ecommerce.Services.EventAnalysisService;
import com.jumbotail.ecommerce.Utils.InMemoryQueue;

import java.util.List;

@RestController
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private InMemoryQueue inMemoryQueue;

    @Autowired
    private EventAnalysisService eventAnalysisService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody List<Event> events) {
        long startTime = System.currentTimeMillis(); // Record the start time

        // Process the batch of events and enqueue them into the InMemoryQueue
        try {
            for (Event event : events) {
                // Retry mechanism: Try pushing the event multiple times in case of failure
                boolean pushSuccess = pushEventWithRetry(event, 3); // Retry 3 times

                if (!pushSuccess) {
                    logger.error("Failed to push event to InMemoryQueue after retrying");
                    return new ResponseEntity<>("Error processing events", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            // Log success and response time
            logger.info("Events processed successfully. API Response Time: {} milliseconds",
                    System.currentTimeMillis() - startTime);

            return new ResponseEntity<>("Events processed successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log error and response time
            logger.error("Error processing events. API Response Time: {} milliseconds",
                    System.currentTimeMillis() - startTime, e);
            return new ResponseEntity<>("Error processing events", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retry mechanism for pushing events to the InMemoryQueue
    private boolean pushEventWithRetry(Event event, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                inMemoryQueue.enqueue(event);
                return true; // Success
            } catch (Exception e) {
                logger.warn("Error pushing event to InMemoryQueue. Retrying...", e);
                retryCount++;
            }
        }
        return false; // Failure after retries
    }

    @GetMapping("/queueContents")
    public ResponseEntity<List<Event>> getQueueContents() {
        try {
            List<Event> queueContents = inMemoryQueue.getQueueContents();
            return new ResponseEntity<>(queueContents, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving queue contents", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/percentageInStage")
    public double getPercentageOfUsersInStage(@RequestParam String stage) {
        return eventAnalysisService.calculatePercentageOfUsersInStage(stage);
    }

    @GetMapping("/percentageInCity")
    public double getPercentageOfUsersInCity(@RequestParam String city) {
        return eventAnalysisService.calculatePercentageOfUsersInCity(city);
    }
}
