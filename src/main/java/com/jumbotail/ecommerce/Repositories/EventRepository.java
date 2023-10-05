package com.jumbotail.ecommerce.Repositories;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.jumbotail.ecommerce.Models.Event;

@Repository
@Qualifier("primaryMongoTemplate")
public interface EventRepository extends MongoRepository<Event, String>{
    @Query(value = "{'eventType': ?0}", count = true)
    long countUsersInStage(String stage);
}
