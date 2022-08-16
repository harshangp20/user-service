package com.tridhyaintuit.user.controller;

import com.tridhyaintuit.user.model.User;
import com.tridhyaintuit.user.service.serviceimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            service.saveUser(user);
            return new ResponseEntity<>("User created !!", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/showAllUsers")
    public List<User> showAllUsers() {
        return service.findAllUsers();
    }

    @GetMapping("/showUser/{id}")
    public User showUserById(@PathVariable String id) {
        return service.findById(id);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String id) {
        try {
            service.updateUser(id, user);
//            System.out.println(id + user);
            return new ResponseEntity<>("User updated !!", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("loginUser/{user}")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            service.loginUser(user);
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        }catch (RuntimeException exception){
            return new ResponseEntity<>("Login Failed",HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("loginUser/{user}")
//    public ResponseEntity<?> loginUser(@RequestBody User user) {
//
//        List<User> users = service.findAllUsers();
//
//        for (User user1 : users) {
//            if (user1.equals(users)) {
//                user.setLogin(true);
//            }
//            service.saveUser(user);
//            return new ResponseEntity<>("Login Successful !!", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Login failed !!!", HttpStatus.BAD_REQUEST);
//    }
}
