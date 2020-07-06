package com.example.demo.model;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface SelectRepository extends CrudRepository<select, Integer> {

    ArrayList<select> findByUserID(String id);
    void deleteByUserIDAndTeamName(String id,String teamName);
}