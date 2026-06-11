package com.example.socialconnect.repository;

import com.example.socialconnect.entity.FriendRequest;
import com.example.socialconnect.entity.FriendRequestStatus;
import com.example.socialconnect.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findByReceiverAndStatusOrderByCreatedAtDesc(User receiver, FriendRequestStatus status);

    List<FriendRequest> findBySenderAndStatusOrderByCreatedAtDesc(User sender, FriendRequestStatus status);

    @Query("""
            SELECT request FROM FriendRequest request
            WHERE request.status = :status
              AND ((request.sender = :firstUser AND request.receiver = :secondUser)
                   OR (request.sender = :secondUser AND request.receiver = :firstUser))
            """)
    Optional<FriendRequest> findBetweenUsersWithStatus(
            @Param("firstUser") User firstUser,
            @Param("secondUser") User secondUser,
            @Param("status") FriendRequestStatus status
    );

    @Query("""
            SELECT request FROM FriendRequest request
            WHERE (request.sender = :firstUser AND request.receiver = :secondUser)
               OR (request.sender = :secondUser AND request.receiver = :firstUser)
            """)
    Optional<FriendRequest> findLatestBetweenUsers(@Param("firstUser") User firstUser, @Param("secondUser") User secondUser);

    @Query("""
            SELECT CASE WHEN request.sender = :user THEN request.receiver ELSE request.sender END
            FROM FriendRequest request
            WHERE request.status = 'ACCEPTED'
              AND (request.sender = :user OR request.receiver = :user)
            ORDER BY request.updatedAt DESC
            """)
    List<User> findFriends(@Param("user") User user);
}
