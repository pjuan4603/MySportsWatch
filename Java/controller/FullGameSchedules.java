package com.example.demo.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
class awayTeam2{
    @JsonProperty("ID")
    String ID;
    @JsonProperty("City")
    String City;
    @JsonProperty("Name")
    String Name;
    @JsonProperty("Abbreviation")
    String Abbreviation;
}

@Data
class homeTeam2{
    @JsonProperty("ID")
    String ID;
    @JsonProperty("City")
    String City;
    @JsonProperty("Name")
    String Name;
    @JsonProperty("Abbreviation")
    String Abbreviation;
}

@Data
class gameentry2{
    String id;
    String date;
    String time;
    @JsonProperty("awayTeam")
    awayTeam2 awayTeam;
    @JsonProperty("homeTeam")
    homeTeam2 homeTeam;
    String location;
}

@Data
class fullgameschedule{
    String lastUpdatedOn;
    @JsonProperty("gameentry")
    ArrayList<gameentry2> gameentry;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class FullGameSchedules{
    fullgameschedule fullgameschedule;
}