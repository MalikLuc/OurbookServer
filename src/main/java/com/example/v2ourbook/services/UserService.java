package com.example.v2ourbook.services;
import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.models.User;
import com.example.v2ourbook.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUsername((String) principal).get();
    }

    public User findById(Long id) throws ExceptionBlueprint {
        return userRepository.findById(id).
                orElseThrow(() -> new ExceptionBlueprint("user not found","no",1));
    }

    public User findByUsername(String name) throws ExceptionBlueprint {
        return userRepository.findUserByUsername(name).
                orElseThrow(() -> new ExceptionBlueprint("user not found","no",1));
    }

//    public void addFriend() {
//
//        Set<User> userUpdatedFriends = currentUser.getFriends();
//        userUpdatedFriends.add(friend);
//        currentUser.setFriends(userUpdatedFriends);
//        userRepository.save(currentUser);
//
//        Set<User> friendUpdatedFriends = friend.getFriends();
//        friendUpdatedFriends.add(currentUser);
//        friend.setFriends(friendUpdatedFriends);
//        userRepository.save(friend);
//    }
}
