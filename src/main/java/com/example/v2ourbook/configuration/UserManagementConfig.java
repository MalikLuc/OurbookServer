package com.example.v2ourbook.configuration;

import com.example.v2ourbook.controller.UserController;
import com.example.v2ourbook.models.Authority;
import com.example.v2ourbook.models.User;
import com.example.v2ourbook.repositories.AuthorityRepository;
import com.example.v2ourbook.repositories.UserRepository;
import com.example.v2ourbook.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserManagementConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Schaut nach ob es den User Eintrag mit username gibt. Erstellt einen neuen User,
     * wenn es ihn noch nicht gibt und weist dem neuen User die authorities zu.
     */
    public User createUserIfNotExists(String username, String password, String[] authorities)
    {

        if (userRepository.existsUserByUsername(username)) {
            return userRepository.findUserByUsername(username).get();
        }

        String encodedPassword = (String) bCryptPasswordEncoder.encode(password);


        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setFirstname(username);
        user.setActive(true);

        userRepository.save(user);

        for (String auth : authorities) {
            Authority authority = new Authority();
            authority.setName(auth);
            authority.setUser(user);
            authorityRepository.save(authority);
        }

        return user;

    }

    @Bean
    public UserDetailsService userDetailsService() {
        createUserIfNotExists("Reminder", "Test123", new String[]{"read", "write", "invite", "login", "ROLE_SUPERADMIN"});
        return new MyUserDetailsService(userRepository);
    }
}
