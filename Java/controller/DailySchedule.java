package com.example.demo.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
class awayTeam{
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
class homeTeam{
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
class gameentry{
    homeTeam homeTeam;
    awayTeam awayTeam;
    String id;
}

@Data
class dailygameschedule{
    String lastUpdatedOn;
    @JsonProperty("gameentry")
    ArrayList<gameentry> gameentry;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DailySchedule{
    dailygameschedule dailygameschedule;
}