package com.example.demo.model;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UserRepository extends CrudRepository<user, Integer> {

    user findByUserID(String id);

}