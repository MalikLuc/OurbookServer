package com.example.v2ourbook.services;


import com.example.v2ourbook.models.CustomUserDetails;
import com.example.v2ourbook.models.User;
import com.example.v2ourbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User user = userRepository.findUserByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Benutzer unbekannt: "+userName));
        return new CustomUserDetails(user);
        
    }

}
