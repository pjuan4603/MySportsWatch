package com.example.demo.model;

import javax.persistence.*;


@Entity // This tells Hibernate to make a table out of this class
@Table(name="selections")
public class select {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String userID;

    private String teamName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String name) {
        this.teamName = name;
    }
}