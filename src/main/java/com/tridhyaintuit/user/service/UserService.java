package com.tridhyaintuit.user.service;

import com.tridhyaintuit.user.model.Age;
import com.tridhyaintuit.user.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface UserService {
    List<User> findAllUsers();
    User findById(String id);
    void saveUser(User user) ;
    Age calculateAge(Date birthDate);
    void updateUser(String id, User user);

    void loginUser(User user);

}
