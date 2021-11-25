package com.example.v2ourbook.repositories;

import com.example.v2ourbook.models.FriendRequest;
import com.example.v2ourbook.enums.RequestResult;
import com.example.v2ourbook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author norvin_klinkmann
 */

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findAllByReceiverAndRequestResult(User receiver, RequestResult requestResult);

    Optional<FriendRequest> findByReceiverAndSenderAndRequestResult(User receiver, User sender, RequestResult requestResult);
}
