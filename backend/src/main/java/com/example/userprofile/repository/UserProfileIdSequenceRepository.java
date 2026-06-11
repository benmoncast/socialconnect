package com.example.userprofile.repository;

import com.example.userprofile.entity.UserProfileIdSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileIdSequenceRepository extends JpaRepository<UserProfileIdSequence, String> {

    @Modifying
    @Query(value = """
            INSERT INTO user_profile_id_sequences (date_key, last_series)
            VALUES (:dateKey, LAST_INSERT_ID(:initialSeries))
            ON DUPLICATE KEY UPDATE last_series = LAST_INSERT_ID(last_series + 1)
            """, nativeQuery = true)
    void reserveNextSeries(@Param("dateKey") String dateKey, @Param("initialSeries") int initialSeries);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getReservedSeries();
}
