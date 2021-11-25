package com.example.v2ourbook.services;

import com.example.v2ourbook.enums.RequestResult;
import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.models.BookClub;
import com.example.v2ourbook.models.BookShelfInviteRequest;
import com.example.v2ourbook.models.User;
import com.example.v2ourbook.repositories.BookClubRepository;
import com.example.v2ourbook.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookClubService {

    BookClubRepository bookClubRepository;

    UserService userService;

    UserRepository userRepository;

    public BookClubService(BookClubRepository bookClubRepository, UserService userService, UserRepository userRepository) {
        this.bookClubRepository = bookClubRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<BookClub> findAllByBookclubOwner(User user){
        return bookClubRepository.findAllByBookclubOwner(user);
    }

    public void save(BookClub bookClub){
        bookClubRepository.save(bookClub);
    }

    public BookClub findById(Long id) throws ExceptionBlueprint {
        return bookClubRepository.findById(id)
                .orElseThrow(() -> new ExceptionBlueprint("bookclub not found","no",1));
    }

    public void delete(BookClub bookClub){
        bookClubRepository.delete(bookClub);
    }

    public void addMember(BookClub bookClub, User newMember){
        bookClub.getBookclubMembers().add(newMember);
        bookClub.setBookclubMembers(bookClub.getBookclubMembers());
        newMember.getBookclubMemberships().add(bookClub);
        newMember.setBookclubMemberships(newMember.getBookclubMemberships());
    }


    public boolean nameTaken(String name) throws ExceptionBlueprint {
        return  bookClubRepository.findByName(name).isPresent();
    }
}
