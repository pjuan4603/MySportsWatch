package com.example.demo.controller;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class team{
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
class Wins{
    @JsonProperty("@category")
    String category;
    @JsonProperty("@abbreviation")
    String abbreviation;
    @JsonProperty("#text")
    String text;
}

@Data
class Losses{
    @JsonProperty("@category")
    String category;
    @JsonProperty("@abbreviation")
    String abbreviation;
    @JsonProperty("#text")
    String text;
}

@Data
class stats{
    @JsonProperty("Wins")
    Wins Wins;
    @JsonProperty("Losses")
    Losses Losses;
}

@Data
class teamstandingsentry{
    team team;
    Long rank;
    stats stats;
}

@Data
class overallteamstandings{
    String lastUpdatedOn;
    @JsonProperty("teamstandingsentry")
    ArrayList<teamstandingsentry> teamstandingsentries;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NBATeamStanding {
    overallteamstandings overallteamstandings;

}