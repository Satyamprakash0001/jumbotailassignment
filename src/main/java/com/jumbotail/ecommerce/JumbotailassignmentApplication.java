package com.jumbotail.ecommerce;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jumbotail.ecommerce.Models.User;
import com.jumbotail.ecommerce.Services.Driver;
import com.jumbotail.ecommerce.Services.EventService;
import com.jumbotail.ecommerce.Services.QueueConsumerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class JumbotailassignmentApplication {

    @Autowired
    private EventService eventService;

    @Autowired
    private QueueConsumerService queueConsumerService;
    
    List<String> cities = new ArrayList<>();

    Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(JumbotailassignmentApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Create a list of 5 cities
        List<String> cities = new ArrayList<>();
        cities.add("City1");
        cities.add("City2");
        cities.add("City3");
        cities.add("City4");
        cities.add("City5");

        // Create a Random object
        Random random = new Random();

        // Simulate multiple users with threads
        for (int userId = 1; userId <= 10; userId++) {
            int randomIndex = random.nextInt(cities.size());
            User user = new User(userId, cities.get(randomIndex));
            boolean flag = queueConsumerService.pushUserToDatabase(user, 3);
            if(flag){
                Driver driver = new Driver(user, eventService);
                Thread thread = new Thread(driver);
                thread.start();
            }
        }

        // Start the QueueConsumerService in a separate thread
        new Thread(() -> queueConsumerService.consumeEvents()).start();
    }
}
