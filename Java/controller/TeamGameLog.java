package com.example.demo.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
class team2{
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
class Wins2{
    @JsonProperty("@category")
    String category;
    @JsonProperty("@abbreviation")
    String abbreviation;
    @JsonProperty("#text")
    String text;
}

@Data
class Losses2{
    @JsonProperty("@category")
    String category;
    @JsonProperty("@abbreviation")
    String abbreviation;
    @JsonProperty("#text")
    String text;
}

@Data
class stats2{
    @JsonProperty("Wins")
    Wins Wins;
    @JsonProperty("Losses")
    Losses Losses;
}

@Data
class game{
    String id;
    String date;
    String time;
    @JsonProperty("awayTeam")
    awayTeam awayTeam;
    @JsonProperty("homeTeam")
    homeTeam homeTeam;
    String location;
}

@Data
class gamelogs{
    game game;
    team team;
    stats stats;
    String winLoss;
}

@Data
class teamgamelogs{
    String lastUpdatedOn;
    @JsonProperty("gamelogs")
    ArrayList<gamelogs> gamelogs;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TeamGameLog {
    teamgamelogs teamgamelogs;

}