package org.example.blogapp.controller;

import org.example.blogapp.dto.CommentDto;
import org.example.blogapp.exception.ApiResponse;
import org.example.blogapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/user/{userId}/post/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable int postId, @RequestBody CommentDto commentDto,@PathVariable int userId) {
        CommentDto createdComment = commentService.createComment(commentDto, postId,userId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
        public ResponseEntity<ApiResponse> deleteComment(@PathVariable int postId, @PathVariable int commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse(true,"comment deleted successfully"),HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable int postId) {
        List<CommentDto> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable int postId, @PathVariable int commentId) {
        CommentDto comment = commentService.getCommentById(postId, commentId);
        return ResponseEntity.ok(comment);
    }
}
