package com.example.demo.controller;

import com.example.demo.model.UserRepository;
import com.example.demo.model.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
    static String nameString;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="userList")
    public String userList(Model model){
        ArrayList<user> userList = new ArrayList<>();
        userList = (ArrayList<user>) userRepository.findAll();

        model.addAttribute("userList", userList);

        return "userList";
    }

    @RequestMapping(value="ban")
    public String banUser(Model model, @RequestParam("id") String userID){

        System.out.println("User "+userID+" has been banned!");

        user u = userRepository.findByUserID(userID);
        u.setStatus("Banned");
        userRepository.save(u);

        return "redirect:userList";
    }

    @RequestMapping(value="unban")
    public String unbanUser(Model model, @RequestParam("id") String userID){

        System.out.println("User "+userID+" has been unbanned!");

        user u = userRepository.findByUserID(userID);
        u.setStatus("Unbanned");
        userRepository.save(u);

        return "redirect:userList";
    }

}
