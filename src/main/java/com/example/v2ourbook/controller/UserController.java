package com.example.v2ourbook.controller;

import com.example.v2ourbook.dto.FireUserDto;
import com.example.v2ourbook.dto.UserDto;
import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.models.*;
import com.example.v2ourbook.repositories.*;
import com.example.v2ourbook.services.FirestoreImageService;
import com.example.v2ourbook.services.LoginService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author norvin_klinkmann
 */

@RestController
public class UserController {

    @Autowired
    LoginService loginService;

    @Autowired
    FirestoreImageService firestoreImageService;

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserController userController;

    @GetMapping("/user/all")
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    // included in crud endpoints
    @GetMapping("/user")
    public FireUserDto user() throws FirebaseAuthException { // TODO clientside 1hour timeout
        System.out.println("Reached");
        User currentUser = userController.getCurrentUser();
        System.out.println(currentUser.getUsername() + " logged in");
        FireUserDto fireUserDto = new FireUserDto(currentUser);
        fireUserDto.setToken(loginService.token(currentUser.getUsername(), currentUser.getAuthorities()));
        System.out.println("has token: "+ fireUserDto.getToken());
        return fireUserDto;
    }

    @PostMapping("/user/pushNotification")
    public void pushNotification(String token) throws FirebaseAuthException { // TODO clientside 1hour timeout
        System.out.println("token: " + token);
        User currentUser = userController.getCurrentUser();
        currentUser.setToken(token);
        userRepository.save(currentUser);
    }

    @PostMapping("/user/uploadProfilePicture")
    public void uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile) { // TODO clientside 1hour timeout
        firestoreImageService.uploadImage(multipartFile);
    }

    // Create a new Note - register a new user
    //@Transactional()
    @PostMapping("/user/register") // TODO change Password
    public ResponseEntity<FireUserDto> register(@RequestBody UserDto user, String password) throws ExceptionBlueprint, FirebaseAuthException {
        String newUserName = user.getUsername();
        if (userRepository.existsUserByUsername(newUserName)) {
            throw new ExceptionBlueprint("usernamealreadyexists","no",2);
        }
        else {
            User newUser = new User();
            if (newUser.isValidUsername(user.getUsername())) {
                if (newUser.isValidPassword(password)){
                    newUser.setUsername(newUserName.toLowerCase());
                    String encodedPassword = (String) bCryptPasswordEncoder.encode(password);
                    newUser.setPassword(encodedPassword);
                    newUser.setFirstname(user.getFirstname());
                    newUser.setLastname(user.getLastname());
                    newUser.setEmail(user.getEmail());
                    newUser.setActive(true);
                    String[] authorities = new String[]{"read", "write", "invite", "login"};
                    for (String auth : authorities) {
                        Authority authority = new Authority();
                        authority.setName(auth);
                        authority.setUser(newUser);
                        newUser.addAuthority(authority);
                        //authorityRepository.save(authority);
                    }
                    System.out.println("Reached");
                    newUser.setToken("");
                    userRepository.save(newUser);
//                    bibRepository.save(bib);
//                    readingListRepository.save(readingList);
                    System.out.println(newUser.toString());
                    FireUserDto fireUserDto = new FireUserDto(newUser);
                    fireUserDto.setToken(loginService.token(newUser.getUsername(), newUser.getAuthorities()));
                    return new ResponseEntity<>(
                            fireUserDto, HttpStatus.OK);
                } else {
                    throw new ExceptionBlueprint("invalidpassword","no",1);
                }
            } else {
                throw new ExceptionBlueprint("invalidusername","no",1);
            }

        }
    }

//    @PostMapping("/user/newContactByUsername")
//    public ResponseEntity<?> newContact(@Valid @RequestBody String username) {
//        if (userRepository.existsUserByUsername(username)) {
//            User userB = userRepository.findUserByUsername(username).get();
//            Friend friend = new Friend();
//            friend.setMe(userController.getCurrentUser());
//            friend.setFriend(userB);
//            friendRepository.save(friend);
//            return ResponseEntity.ok().build();
//        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }

    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUsername((String) principal).get();
    }

    public UserDto getCurrentRestUser(){
        return new UserDto(this.getCurrentUser());
    }

    // Get a Single Note
    @GetMapping("/user/getUserById/{id}")
    public UserDto getUserById(@PathVariable(value = "id") Long userId) throws ExceptionBlueprint {
        return new UserDto(userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionBlueprint("usernotfound","no",1)));
    }

    @GetMapping("/user/getUserByName/{name}")
    public UserDto getUserByName(@PathVariable(value = "name") String username) throws ExceptionBlueprint {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ExceptionBlueprint("usernotfound", "no", 1));
        return new UserDto(user);
    }

    @GetMapping("/user/getUserByNameFiltered/{term}")
    public List<UserDto> getUsersByTerm(@PathVariable(value = "term") String term) {
        List<UserDto> list = new java.util.ArrayList<>();
        for (User u :
                userRepository.findAllByUsernameContains(term)) {
            list.add(new UserDto(u));
        }
        return list;
    }

    // Update a Note
    @PutMapping("/user")
    public ResponseEntity<?> updateNote(@Valid @RequestBody UserDto userDetails) throws ExceptionBlueprint {
        User user = userController.getCurrentUser();
        user.setUsername(userDetails.getUsername());
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setEmail(userDetails.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // Delete a Note
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser() throws ExceptionBlueprint {
        userRepository.delete(userController.getCurrentUser());
        return ResponseEntity.ok().build();
    }

}
