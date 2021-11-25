package com.example.v2ourbook.controller;

import com.example.v2ourbook.error.UserNotFoundException;
import com.example.v2ourbook.models.User;
import com.example.v2ourbook.repositories.UserRepository;
import com.example.v2ourbook.utils.ReturnError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

class ReturnPasswordToken {

    String token;
    Date expires;

    public ReturnPasswordToken() {
    }

    public ReturnPasswordToken(String token, Date expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}

@RestController
public class ResetPasswordController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/resetPassword/{name}")
    public Object resetPassword(@PathVariable(value = "name") String name) throws UserNotFoundException {

//        User user = userRepository.findUserByUsernameOrEmail(name,name).orElseThrow(() ->
//            new UserNotFoundException("resetPassd: "+name)
//        );

                Optional<User> userO = userRepository.findUserByUsernameOrEmail(name,name);

                if (userO.isEmpty()) {
                    return new ReturnError("resetPassword", "username or email not found",null);
                }

                User user = userO.get();

        user.setPasswordResetToken(UUID.randomUUID().toString());
        user.setPasswordResetTokenDate(new Date());
        userRepository.save(user);

        return new ReturnPasswordToken(user.getPasswordResetToken(),user.getPasswordResetTokenDate());

    }
}
