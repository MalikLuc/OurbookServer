package com.example.v2ourbook.services;

import com.example.v2ourbook.enums.BookStatus;
import com.example.v2ourbook.enums.VisibilityType;
import com.example.v2ourbook.models.*;
import com.example.v2ourbook.repositories.BookClubRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    UserService userService;

    BookClubRepository bookclubRepository;

    public BookService(BookClubRepository bookclubRepository, UserService userService) {
//        super();
        this.bookclubRepository =  bookclubRepository;
        this.userService = userService;
    }

    public List<Book> getBooksForBookClub(Long id){
        List<Book> books = new java.util.ArrayList<>();
        bookclubRepository.findBookClubById(id).getBookclubMembers().
                forEach(m -> books.addAll(m.getBooks()));
        return visibilityFilter(books);
    }

    public Book extractBookFromResponse(ResponseEntity<String> response, User user, String isbn) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        String title = root.at("/items/0/volumeInfo/title").asText();
        String author = root.at("/items/0/volumeInfo/authors/0").asText();
        String erscheinungsDatum = root.at("/items/0/volumeInfo/publishedDate").asText();
        String sprache = root.at("/items/0/volumeInfo/language").asText();
        String pictureUrl = root.at("/items/0/volumeInfo/imageLinks/thumbnail").asText();
        String description = root.at("/items/0/volumeInfo/description").asText();
//        String  = root.at("/items/0/volumeInfo/description").asText();
//        String auflage = root.at("/items/0/volumeInfo/title").asText();
        URL url = new URL("https://www.google.com.pk/images/srpr/logo4w.png");
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        BufferedImage image = ImageIO.read(in);
        int width = image.getWidth();
        int height = image.getHeight();
        Book book = new Book();
        book.setCoverWidth(width);
        book.setCoverHeight(height);
        book.setUser(user);
        book.setIsbn(isbn);
        book.setPictureUrl(pictureUrl);
        book.setTitel(title);
        book.setAuthorName(author);
        book.setErscheinungsDatum(erscheinungsDatum);
        book.setSprache(sprache);
        book.setDescription(description);
        book.setStatus(BookStatus.AVAILABLE);
        return book;
    }

    // removes books from the List that should not be visible
    public List<Book> visibilityFilter(List<Book> books){
        User currentUser = userService.getCurrentUser();
        // loop through all the books and get the visibility type and filter the ones that dont match the visible function
        return books.stream().filter(b -> visible(b)).collect(Collectors.toList());
    }

    public boolean visible(Book book){
        User currentUser = userService.getCurrentUser();
        if (book.getVisibilityType().equals(VisibilityType.USER)){
            return true;
        }
        if (book.getVisibilityType().equals(VisibilityType.BOOKCLUBS)){
            return !Collections.disjoint(book.getUserLocation().getBookclubMemberships(), currentUser.getBookclubMemberships()); // TODO Test this
        } else {
            return book.getUserLocation().getFriends().contains(currentUser);
        }
    }

    public ResponseEntity<String> lookUpByIsbn(String isbn){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
        return restTemplate.getForEntity(fooResourceUrl + isbn, String.class);
    }
}
