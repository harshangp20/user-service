package com.tridhyaintuit.user.service.serviceimpl;

import com.mongodb.client.result.UpdateResult;

import com.tridhyaintuit.user.model.Age;
import com.tridhyaintuit.user.model.User;
import com.tridhyaintuit.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private MongoTemplate template;

    @Override
    public List<User> findAllUsers() {
        return template.findAll(User.class);
    }

    @Override
    public User findById(String id) {
        return template.findById(id, User.class);
    }

    @Override
    public void saveUser(User userData) {
        boolean userEmailExists = template.query(User.class)
                .matching(Query.query(Criteria.where("email").in(userData.getEmail()))
                        .addCriteria(Criteria.where("deleted").is(false))).exists();

        Age age = calculateAge(userData.getBirthDate());
        BCryptPasswordEncoder passwordEncrypt = new BCryptPasswordEncoder();
        String hashPwd = passwordEncrypt.encode(userData.getPassWord());
        User user = new User();
        if(userData.getName() != null){
        user.setName(userData.getName());
        }
        user.setCreated(new Date());
        user.setBirthDate(userData.getBirthDate());
        user.setAddress(userData.getAddress());
        user.setPinCode(userData.getPinCode());
        user.setPhone(userData.getPhone());
        if (!userEmailExists) {
            user.setEmail(userData.getEmail());
        } else {
            throw new RuntimeException("Email Exists!!!");
        }
        user.setAge(age);
        if (userData.getPassWord() !=null) {
            user.setPassWord(hashPwd);
        }else {
            System.out.println("Please Enter a valid password");
        }
        System.out.println("user: " + user);

        template.save(user);
    }

    @Override
    public void updateUser(String id, User userData) {
        Query query = new Query(Criteria.where("_id").is(id));
        query.addCriteria(Criteria.where("deleted").is(false));
        System.out.println(query);

        Update update = new Update();
        if (userData.getName() != null) {
            update.set("name", userData.getName());
        }
        if (userData.getBirthDate() != null) {
            Age age = calculateAge(userData.getBirthDate());
            update.set("birthDate", userData.getBirthDate());
            update.set("age", age);
        }
        if (userData.getPhone() != null) {
            update.set("phone", userData.getPhone());
        }
        if (userData.getAddress() != null) {
            update.set("address", userData.getAddress());
        }
        if (userData.getPinCode() != null) {
            update.set("pinCode", userData.getPinCode());
        }
        if (userData.getLastUpdated() == null) {
            update.set("lastUpdated", new Date());
        }

        if (!userData.isDeleted()) {
            update.set("deleted flag", true);
        }
        System.out.println(update);
        UpdateResult result = template.updateFirst(query, update, User.class, "User");

        System.out.println(result);

    }

    @Override
    public void loginUser(User user) {

        BCryptPasswordEncoder passwordEncrypt = new BCryptPasswordEncoder();
        String hashPwd = passwordEncrypt.encode(user.getPassWord());

        User user1 = new User();
        user1.getEmail();
        user1.getPassWord();

        if(user1.getPassWord().matches(hashPwd)) {
            boolean isPasswordMatch = passwordEncrypt.matches(hashPwd, user1.getPassWord());
            System.out.println(isPasswordMatch);
        }
//        else {
//            throw new RuntimeException("You can't Login");
//        }
    }


    @Override
    public Age calculateAge(Date birthDate)
    {
        int years;
        int months;
        int days;

        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        if (months < 0)
        {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
        {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        }
        else
        {
            days = 0;
            if (months == 12)
            {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }
}