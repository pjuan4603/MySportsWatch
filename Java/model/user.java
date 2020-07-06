package com.example.demo.model;

import javax.persistence.*;


@Entity // This tells Hibernate to make a table out of this class
@Table(name="user")
public class user {

    @Id
    private String userID;

    private String status;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}