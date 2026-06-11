package com.example.socialconnect.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.socialconnect.dto.CommentRequest;
import com.example.socialconnect.dto.PostRequest;
import com.example.socialconnect.entity.Comment;
import com.example.socialconnect.entity.Post;
import com.example.socialconnect.entity.PostPrivacy;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.CommentRepository;
import com.example.socialconnect.repository.PostRepository;
import com.example.socialconnect.repository.ReactionRepository;
import com.example.socialconnect.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class PostServiceAuthorizationTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private FriendService friendService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UploadService uploadService;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private PostService postService;

    @Test
    void updatePostRejectsNonOwner() {
        User owner = user(1L);
        User attacker = user(2L);
        Post post = post(owner, PostPrivacy.PUBLIC);

        when(currentUserService.getCurrentUser()).thenReturn(attacker);
        when(postRepository.findById(100L)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> postService.updatePost(100L, new PostRequest("edited", null, PostPrivacy.PUBLIC)))
                .isInstanceOfSatisfying(AppException.class, ex ->
                        assertThat(ex.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    @Test
    void deleteCommentRejectsNonOwner() {
        User owner = user(1L);
        User attacker = user(2L);
        Comment comment = Comment.builder()
                .id(200L)
                .user(owner)
                .content("comment")
                .build();

        when(currentUserService.getCurrentUser()).thenReturn(attacker);
        when(commentRepository.findById(200L)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> postService.deleteComment(200L))
                .isInstanceOfSatisfying(AppException.class, ex ->
                        assertThat(ex.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    @Test
    void addCommentRejectsPrivatePostForNonFriend() {
        User owner = user(1L);
        User stranger = user(2L);
        Post post = post(owner, PostPrivacy.FRIENDS);

        when(currentUserService.getCurrentUser()).thenReturn(stranger);
        when(postRepository.findById(100L)).thenReturn(Optional.of(post));
        when(friendService.areFriends(stranger, owner)).thenReturn(false);

        assertThatThrownBy(() -> postService.addComment(100L, new CommentRequest("hello")))
                .isInstanceOfSatisfying(AppException.class, ex ->
                        assertThat(ex.getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    private User user(Long id) {
        return User.builder().id(id).email("user" + id + "@example.test").username("user" + id).build();
    }

    private Post post(User owner, PostPrivacy privacy) {
        return Post.builder()
                .id(100L)
                .user(owner)
                .content("post")
                .privacy(privacy)
                .build();
    }
}
