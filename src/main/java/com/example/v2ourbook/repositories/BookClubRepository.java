package com.example.v2ourbook.repositories;
import com.example.v2ourbook.models.BookClub;
import com.example.v2ourbook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@RepositoryRestResource(path="authorities")
@Repository
public interface BookClubRepository extends JpaRepository<BookClub,Long> {
    List<BookClub> findAllByBookclubOwner(User bookclubOwner);

    BookClub findBookClubById(Long id);

    Optional<BookClub> findByName(String name);
}
