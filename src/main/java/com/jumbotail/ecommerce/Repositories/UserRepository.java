package com.jumbotail.ecommerce.Repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.jumbotail.ecommerce.Models.User;

@Repository
@Qualifier("secondaryMongoTemplate")
public interface UserRepository extends MongoRepository<User, String>{
    // Custom query to count users in a specific city
    @Query(value = "{'city': ?0}", count = true)
    long countUsersInCity(String city);
}
