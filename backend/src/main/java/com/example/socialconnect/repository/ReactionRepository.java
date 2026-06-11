package com.example.socialconnect.repository;

import com.example.socialconnect.entity.Post;
import com.example.socialconnect.entity.Reaction;
import com.example.socialconnect.entity.ReactionType;
import com.example.socialconnect.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByPostAndUser(Post post, User user);

    List<Reaction> findByPost(Post post);

    long countByPostAndType(Post post, ReactionType type);

    void deleteByPostAndUser(Post post, User user);
}
