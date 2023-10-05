package com.jumbotail.ecommerce.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    
    private int userId;
    private String city;
    
    public User(){}

    public User(String id, int userId, String city) {
        this.id = id;
        this.userId = userId;
        this.city = city;
    }

    public User(int userId, String city) {
        this.userId = userId;
        this.city = city;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", city=" + city + "]";
    }

    public String getId() {
        return id;
    }
    
}
