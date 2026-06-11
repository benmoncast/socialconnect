package com.example.socialconnect.service;

import com.example.socialconnect.dto.CommentDto;
import com.example.socialconnect.dto.CommentRequest;
import com.example.socialconnect.dto.PostDto;
import com.example.socialconnect.dto.PostRequest;
import com.example.socialconnect.dto.ReactionRequest;
import com.example.socialconnect.dto.UploadResponse;
import com.example.socialconnect.entity.Comment;
import com.example.socialconnect.entity.NotificationType;
import com.example.socialconnect.entity.Post;
import com.example.socialconnect.entity.PostPrivacy;
import com.example.socialconnect.entity.Reaction;
import com.example.socialconnect.entity.User;
import com.example.socialconnect.exception.AppException;
import com.example.socialconnect.repository.CommentRepository;
import com.example.socialconnect.repository.PostRepository;
import com.example.socialconnect.repository.ReactionRepository;
import com.example.socialconnect.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final CurrentUserService currentUserService;
    private final FriendService friendService;
    private final NotificationService notificationService;
    private final UploadService uploadService;
    private final DtoMapper dtoMapper;

    public PostDto createPost(PostRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        if (!StringUtils.hasText(request.content()) && !StringUtils.hasText(request.imageUrl())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Post content or image is required");
        }

        Post post = postRepository.save(Post.builder()
                .user(currentUser)
                .content(request.content())
                .imageUrl(request.imageUrl())
                .privacy(request.privacy() == null ? PostPrivacy.PUBLIC : request.privacy())
                .build());
        return dtoMapper.toPostDto(post, currentUser, "SELF");
    }

    public PostDto updatePost(Long postId, PostRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        requirePostOwner(post, currentUser);

        post.setContent(request.content());
        post.setImageUrl(request.imageUrl());
        post.setPrivacy(request.privacy() == null ? PostPrivacy.PUBLIC : request.privacy());
        return dtoMapper.toPostDto(postRepository.save(post), currentUser, "SELF");
    }

    public void deletePost(Long postId) {
        User currentUser = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        requirePostOwner(post, currentUser);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getFeed() {
        User currentUser = currentUserService.getCurrentUser();
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(post -> canViewPost(currentUser, post))
                .map(post -> dtoMapper.toPostDto(post, currentUser, friendService.friendshipStatus(currentUser, post.getUser())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostDto> getUserPosts(Long userId) {
        User currentUser = currentUserService.getCurrentUser();
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

        return postRepository.findByUserOrderByCreatedAtDesc(owner)
                .stream()
                .filter(post -> canViewPost(currentUser, post))
                .map(post -> dtoMapper.toPostDto(post, currentUser, friendService.friendshipStatus(currentUser, owner)))
                .toList();
    }

    public CommentDto addComment(Long postId, CommentRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        if (!canViewPost(currentUser, post)) {
            throw new AppException(HttpStatus.FORBIDDEN, "You cannot comment on this post");
        }

        Comment comment = commentRepository.save(Comment.builder()
                .post(post)
                .user(currentUser)
                .content(request.content())
                .build());

        notificationService.notify(post.getUser(), currentUser, NotificationType.POST_COMMENT,
                dtoMapper.fullName(currentUser) + " commented on your post.");
        return dtoMapper.toCommentDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long postId) {
        User currentUser = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        if (!canViewPost(currentUser, post)) {
            throw new AppException(HttpStatus.FORBIDDEN, "You cannot view comments on this post");
        }

        return commentRepository.findByPostOrderByCreatedAtAsc(post)
                .stream()
                .map(dtoMapper::toCommentDto)
                .toList();
    }

    public CommentDto updateComment(Long commentId, CommentRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Comment comment = getComment(commentId);
        requireCommentOwner(comment, currentUser);
        comment.setContent(request.content());
        return dtoMapper.toCommentDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        User currentUser = currentUserService.getCurrentUser();
        Comment comment = getComment(commentId);
        requireCommentOwner(comment, currentUser);
        commentRepository.delete(comment);
    }

    public PostDto reactToPost(Long postId, ReactionRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Post post = getPost(postId);
        if (!canViewPost(currentUser, post)) {
            throw new AppException(HttpStatus.FORBIDDEN, "You cannot react to this post");
        }

        Reaction reaction = reactionRepository.findByPostAndUser(post, currentUser)
                .orElseGet(() -> Reaction.builder().post(post).user(currentUser).build());
        reaction.setType(request.type());
        reactionRepository.save(reaction);

        notificationService.notify(post.getUser(), currentUser, NotificationType.POST_REACTION,
                dtoMapper.fullName(currentUser) + " reacted to your post.");
        return dtoMapper.toPostDto(post, currentUser, friendService.friendshipStatus(currentUser, post.getUser()));
    }

    public void removeReaction(Long postId) {
        User currentUser = currentUserService.getCurrentUser();
        reactionRepository.deleteByPostAndUser(getPost(postId), currentUser);
    }

    public UploadResponse uploadPostImage(MultipartFile file) {
        return new UploadResponse(uploadService.store(file));
    }

    private boolean canViewPost(User currentUser, Post post) {
        if (post.getUser().getId().equals(currentUser.getId())) {
            return true;
        }
        if (post.getPrivacy() == PostPrivacy.PUBLIC) {
            return true;
        }
        return post.getPrivacy() == PostPrivacy.FRIENDS && friendService.areFriends(currentUser, post.getUser());
    }

    private void requirePostOwner(Post post, User currentUser) {
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only change your own posts");
        }
    }

    private void requireCommentOwner(Comment comment, User currentUser) {
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only change your own comments");
        }
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    private Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Comment not found"));
    }
}
