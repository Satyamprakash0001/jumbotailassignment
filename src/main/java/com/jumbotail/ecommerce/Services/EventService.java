package com.jumbotail.ecommerce.Services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jumbotail.ecommerce.Models.Event;
import com.jumbotail.ecommerce.Models.User;
import com.jumbotail.ecommerce.Repositories.EventRepository;


@Service
public class EventService {
    // @Autowired
    private final EventRepository eventRepository;
    // @Autowired
    private final RestTemplate restTemplate;

    @Value("${webhook.url}")
    private String webhookUrl;

    public EventService(EventRepository eventRepository, RestTemplate restTemplate) {
        this.eventRepository = eventRepository;
        this.restTemplate = restTemplate;
    }

    public Event generateEvent(int userId, String city) {
        User user = new User(userId, city);
        
        // Simulate event generation based on user behavior
        String eventType;
        switch ((int) (Math.random() * 5)) {
            case 0:
                eventType = "AccessApp";
                break;
            case 1:
                eventType = "ClickBanner";
                break;
            case 2:
                eventType = "ViewProducts";
                break;
            case 3:
                eventType = "SelectProduct";
                break;
            case 4:
                eventType = "AddToCart";
                break;
            default:
                eventType = "AccessApp"; // Default to AccessApp
        }

        return new Event(eventType, user, System.currentTimeMillis());
    }

    public List<Event> batchGenerateEvents(User user, int batchSize) {
        List<Event> eventBatch = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            Event event = generateEvent(user.getUserId(), user.getCity());
            eventRepository.save(event);
            eventBatch.add(event);
        }
        return eventBatch;
    }

    public void invokeWebhook(List<Event> eventBatch) {
        // Simulate invoking the webhook by sending batches of events
        URI uri = null;
        try {
            uri = new URI(webhookUrl);
            restTemplate.postForEntity(uri, eventBatch, String.class);
        } catch (URISyntaxException e) {
            System.err.println("Invalid webhook URL: " + webhookUrl);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error invoking webhook at " + uri);
            e.printStackTrace();
        }
    }

    public void logApiResponseTime(long startTime) {
        long responseTime = System.currentTimeMillis() - startTime;
        System.out.println("API Response Time: " + responseTime + " milliseconds");
    }

    public void processEventAsync(Event event) {
        // Simulate asynchronous processing (replace this with your actual processing logic)
        new Thread(() -> {
            try {
                // Simulate processing time (replace this with your actual processing logic)
                Thread.sleep((int) (Math.random() * 2000) + 1000);
                System.out.println("Processed event asynchronously: " + event.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
