package com.example.userprofile.repository;

import com.example.userprofile.entity.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUserId(Long userId);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByUserIdAndIdNot(Long userId, Long id);

    @Query("SELECT MAX(profile.userId) FROM UserProfile profile WHERE profile.userId BETWEEN :lowerBound AND :upperBound")
    Optional<Long> findLatestGeneratedUserIdForDate(
            @Param("lowerBound") Long lowerBound,
            @Param("upperBound") Long upperBound
    );

    @Query("""
            SELECT profile FROM UserProfile profile
            WHERE LOWER(profile.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(profile.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(profile.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(profile.city) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(profile.province) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(profile.country) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<UserProfile> search(@Param("keyword") String keyword);
}
