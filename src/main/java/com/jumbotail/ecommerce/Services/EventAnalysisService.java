package com.jumbotail.ecommerce.Services;

import com.jumbotail.ecommerce.Repositories.EventRepository;
import com.jumbotail.ecommerce.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EventAnalysisService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventAnalysisService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public double calculatePercentageOfUsersInStage(String stage) {
        // Query the database to get the count of users in the specified stage
        long usersInStageCount = eventRepository.countUsersInStage(stage);

        // Query the total count of users
        long totalEventsCount = eventRepository.count();

        // Calculate the percentage
        if (totalEventsCount > 0) {
            return (usersInStageCount * 100.0) / totalEventsCount;
        } else {
            return 0.0; // Avoid division by zero
        }
    }

    public double calculatePercentageOfUsersInCity(String city) {
        // Query the database to get the count of users in the specified city
        long usersInCityCount = userRepository.countUsersInCity(city);
        // Query the total count of users
        long totalUsersCount = userRepository.count();

        // Calculate the percentage
        if (totalUsersCount > 0) {
            return (usersInCityCount * 100.0) / totalUsersCount;
        } else {
            return 0.0; // Avoid division by zero
        }
    }

    // You may add other analysis methods as needed
}
