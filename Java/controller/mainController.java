package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import  com.example.demo.model.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Transactional
@Controller
public class mainController {

    static String nameString, userID;
    static int loginStatus=0,userStatus=0;

    @Autowired
    private SelectRepository selectRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="")
    public String index(Model model){
        System.out.println(userID+"!"+loginStatus);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        System.out.println(formatter.format(date));

        String url ="https://api.mysportsfeeds.com/v1.0/pull/nba/2018-2019-regular/daily_game_schedule.json?fordate="+formatter.format(date);
        String encoding = Base64.getEncoder().encodeToString("96c5a8df-8829-4b52-ac58-2b04cb:alone4603".getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic "+encoding);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DailySchedule> response = restTemplate.exchange(url, HttpMethod.GET, request, DailySchedule.class);
        DailySchedule ds = response.getBody();
        System.out.println(ds.toString());

        model.addAttribute("dailySchedule",ds.getDailygameschedule().getGameentry());
        model.addAttribute("teamList",selectRepository.findByUserID(userID));
        if(userStatus==3) {
            model.addAttribute("show", true);
        }
        else{
            model.addAttribute("show",false);
        }
        if(loginStatus==0 || userStatus==2) {
            model.addAttribute("loginShow", false);
        }
        else{
            model.addAttribute("loginShow",true);
        }


        return "home";
    }

    @RequestMapping(value="favteam" , method= RequestMethod.GET)
    public String favTeam(){
        if(loginStatus==0){
            System.out.println("return loginpage");
            return "redirect:login";
        }
        if(userStatus==2){
            return "bannedPage";
        }

        return "favTeam";
    }

    @RequestMapping(value="favteam", method = RequestMethod.POST)
    public String displayAndStore(@RequestParam String tName){

        nameString = tName;

        ArrayList<select> s = selectRepository.findByUserID(userID);
        HashMap<String, String> hmap = new HashMap<String, String>();
        int i = 0;
        while(i<s.size()){
            hmap.put(s.get(i).getTeamName(),"");
            i++;
        }

        String tokens[] = tName.split(",");
        for(i=0;i<tokens.length;i++){

            if(!hmap.containsKey(tokens[i])){
                select s2 = new select();
                s2.setUserID(userID);
                s2.setTeamName(tokens[i]);
                selectRepository.save(s2);
            }
        }
        return "redirect:";
    }

    @RequestMapping(value="logout")
    public String logout(){
        if(loginStatus==0){
            return "loginPage";
        }
        return "logoutPage";
    }

    @RequestMapping(value="logout", method = RequestMethod.POST)
    public String backHome(){
        loginStatus=0;
        //userID = "Login";
        return "home";
    }

    @RequestMapping(value="login")
    public String login(Model model){
        if(loginStatus==1){
            loginStatus=0;
            userStatus=0;
            userID=null;
            return "logoutPage";
        }
        return "loginPage";
    }

    @RequestMapping(value="login", method = RequestMethod.POST)
    public String setUpLogin(@RequestParam("userID") String userID){

        loginStatus=1;
        this.userID = userID;

        if(userRepository.findByUserID(userID)==null){
            user u = new user();
            u.setUserID(userID);
            u.setStatus("Unbanned");
            userRepository.save(u);
            System.out.println("New User: "+userID);
            userStatus = 1;
        }else{
            System.out.println("Welcome back!");
            user u = userRepository.findByUserID(userID);
            if(u.getStatus().equals("Banned")){
                userStatus=2;
                return "bannedPage";

            }else if(u.getStatus().equals("Admin")){
                userStatus=3;
            }else{
                userStatus=1;
            }
        }

        return "redirect:";
    }

    @RequestMapping(value="myfavteam")
    public String myfavteam(Model model){
        if(loginStatus==0){
            return "loginPage";
        }
        if(userStatus==2){
            return "bannedPage";
        }

        model.addAttribute("teamList",selectRepository.findByUserID(userID));
        return "myfavTeam";
    }

    @RequestMapping(value="remove")
    public String removemyfavteam(Model model, @RequestParam("teamName") String teamName){

        System.out.println("Team "+teamName+" has been removed!");

        selectRepository.deleteByUserIDAndTeamName(userID,teamName);

        return "redirect:myfavteam";
    }

    @RequestMapping(value="ranking")
    public String ranking(Model model){

        //Endpoint to call
        String url ="https://api.mysportsfeeds.com/v1.2/pull/nba/2018-2019-regular/overall_team_standings.json";
        //Encode Username and Password
        String encoding = Base64.getEncoder().encodeToString("96c5a8df-8829-4b52-ac58-2b04cb:alone4603".getBytes());
        // TOKEN:PASS
        //Add headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic "+encoding);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        //Make the call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NBATeamStanding> response = restTemplate.exchange(url, HttpMethod.GET, request, NBATeamStanding.class);
        NBATeamStanding ts = response.getBody();
        System.out.println(ts.toString());
        //Send the object to view
        model.addAttribute("teamStandingEntries", ts.getOverallteamstandings().getTeamstandingsentries());

        return "ranking";
    }

    @RequestMapping(value="teamProfile")
    public String teamProfile(Model model, @RequestParam("id") String teamID){

        System.out.println(teamID);

        //Endpoint to call
        String url ="https://api.mysportsfeeds.com/v1.2/pull/nba/2018-2019-regular/overall_team_standings.json?team="+teamID ;
        //Encode Username and Password
        String encoding = Base64.getEncoder().encodeToString("96c5a8df-8829-4b52-ac58-2b04cb:alone4603".getBytes());
        // TOKEN:PASS
        //Add headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic "+encoding);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        //Make the call
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NBATeamStanding> response = restTemplate.exchange(url, HttpMethod.GET, request, NBATeamStanding.class);
        NBATeamStanding ts = response.getBody();
        System.out.println(ts.toString());
        //Send the object to view
        model.addAttribute("teamStandingEntries", ts.getOverallteamstandings().getTeamstandingsentries());
        String teamName = ts.getOverallteamstandings().getTeamstandingsentries().get(0).getTeam().getName();
        model.addAttribute("teamName", teamName);
        System.out.println(teamName);
        url ="https://api.mysportsfeeds.com/v1.2/pull/nba/2018-2019-regular/full_game_schedule.json?team="+teamID ;
        //Encode Username and Password
        encoding = Base64.getEncoder().encodeToString("96c5a8df-8829-4b52-ac58-2b04cb:alone4603".getBytes());
        // TOKEN:PASS
        //Add headers
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic "+encoding);
        request = new HttpEntity<String>(headers);

        //Make the call
        restTemplate = new RestTemplate();
        ResponseEntity<FullGameSchedules> response2 = restTemplate.exchange(url, HttpMethod.GET, request, FullGameSchedules.class);
        FullGameSchedules fgs = response2.getBody();
        System.out.println(fgs.toString());
        //Send the object to view
        model.addAttribute("fullGameSchedule", fgs.getFullgameschedule().getGameentry());

        url ="https://api.mysportsfeeds.com/v1.2/pull/nba/2018-2019-regular/team_gamelogs.json?team="+teamID ;
        //Encode Username and Password
        encoding = Base64.getEncoder().encodeToString("96c5a8df-8829-4b52-ac58-2b04cb:alone4603".getBytes());
        // TOKEN:PASS
        //Add headers
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic "+encoding);
        request = new HttpEntity<String>(headers);

        //Make the call
        restTemplate = new RestTemplate();
        ResponseEntity<TeamGameLog> response3 = restTemplate.exchange(url, HttpMethod.GET, request, TeamGameLog.class);
        TeamGameLog tgs = response3.getBody();
        System.out.println(tgs.toString());
        //Send the object to view

        int i=0;
        while(i<tgs.getTeamgamelogs().getGamelogs().size()){
            if(tgs.getTeamgamelogs().getGamelogs().get(i).getStats().getWins().text.equals("1")){
                tgs.getTeamgamelogs().getGamelogs().get(i).setWinLoss("W");
            }else{
                tgs.getTeamgamelogs().getGamelogs().get(i).setWinLoss("L");
            }
            i++;
            tgs.getTeamgamelogs().getGamelogs().iterator().next();
        }

        model.addAttribute("teamGameLog", tgs.getTeamgamelogs().getGamelogs());
        return "teamProfile";
    }

    @RequestMapping(value="teamProfile", method = RequestMethod.POST)
    public String addFavorite(Model model,@RequestParam("teamName") String teamName){

        if(loginStatus==0){
            return "loginPage";
        }
        if(userStatus==2){
            return "bannedPage";
        }

        ArrayList<select> s = selectRepository.findByUserID(userID);
        HashMap<String, String> hmap = new HashMap<String, String>();
        int i = 0;
        while(i<s.size()){
            hmap.put(s.get(i).getTeamName(),"");
            i++;
        }

       if(!hmap.containsKey(teamName)){
           select s2 = new select();
           s2.setUserID(userID);
           s2.setTeamName(teamName);
           selectRepository.save(s2);
       }

       return "redirect:/";
    }

    @RequestMapping(value="admin")
    public String adminHome(){
        if(userStatus!=3){
            return "redirect:/";
        }

        return  "redirect:/admin/userList";

    }
}
