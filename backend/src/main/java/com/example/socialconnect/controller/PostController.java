package com.example.socialconnect.controller;

import com.example.socialconnect.dto.CommentDto;
import com.example.socialconnect.dto.CommentRequest;
import com.example.socialconnect.dto.PostDto;
import com.example.socialconnect.dto.PostRequest;
import com.example.socialconnect.dto.ReactionRequest;
import com.example.socialconnect.dto.UploadResponse;
import com.example.socialconnect.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @GetMapping("/posts/feed")
    public ResponseEntity<List<PostDto>> getFeed() {
        return ResponseEntity.ok(postService.getFeed());
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getUserPosts(userId));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/images")
    public ResponseEntity<UploadResponse> uploadPostImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(postService.uploadPostImage(file));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addComment(postId, request));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getComments(postId));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(postService.updateComment(commentId, request));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        postService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/reactions")
    public ResponseEntity<PostDto> reactToPost(
            @PathVariable Long postId,
            @Valid @RequestBody ReactionRequest request
    ) {
        return ResponseEntity.ok(postService.reactToPost(postId, request));
    }

    @DeleteMapping("/posts/{postId}/reactions")
    public ResponseEntity<Void> removeReaction(@PathVariable Long postId) {
        postService.removeReaction(postId);
        return ResponseEntity.noContent().build();
    }
}
