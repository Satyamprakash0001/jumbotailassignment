package com.jumbotail.ecommerce.Models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event {
    @Id
    private String id;

    private String eventType;
    private User user;
    private long timestamp;

    public Event(){}

    public Event(String id, String eventType, User user, long timestamp) {
        this.id = id;
        this.eventType = eventType;
        this.user = user;
        this.timestamp = timestamp;
    }

    public Event(String eventType, User user, long timestamp) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.user = user;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", eventType=" + eventType + ", user=" + user + ", timestamp=" + timestamp + "]";
    }
    
}
