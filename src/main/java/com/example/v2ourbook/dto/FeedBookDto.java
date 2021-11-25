package com.example.v2ourbook.dto;

import com.example.v2ourbook.models.Book;

public class FeedBookDto {

    BookDto book;
    UserDto user;
    String message;
    Long timeCreated;


    public FeedBookDto(Book book) {
        this.setMessage(book.getTitel() + "");
        this.setTimeCreated(book.getTimeCreated().getTime());
//        this.setUser(new UserDto(book.getUser()));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
