package com.example.socialconnect.repository;

import com.example.socialconnect.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("""
            SELECT user FROM User user
            WHERE LOWER(user.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(user.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(user.username) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(user.email) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY user.firstName ASC, user.lastName ASC
            """)
    List<User> searchUsers(@Param("query") String query);
}
