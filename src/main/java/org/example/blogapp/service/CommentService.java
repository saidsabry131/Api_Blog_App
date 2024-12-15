package org.example.blogapp.service;

import org.example.blogapp.dto.CommentDto;
import org.example.blogapp.dto.PostDto;
import org.example.blogapp.entity.Comment;
import org.example.blogapp.entity.Post;
import org.example.blogapp.entity.User;
import org.example.blogapp.exception.ResourceNotFound;
import org.example.blogapp.repository.CommentRepo;
import org.example.blogapp.repository.PostRepo;
import org.example.blogapp.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepo commentRepo;

    private final PostRepo postRepo;

    private final ModelMapper mapper;

    private final UserRepo userRepo;

    public CommentService(CommentRepo commentRepo, PostRepo postRepo, ModelMapper mapper, UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.mapper = mapper;
        this.userRepo = userRepo;
    }

    public CommentDto createComment(CommentDto commentDto,int postId,int userId)
    {
        Post post= postRepo.findById(postId).orElseThrow(()->new ResourceNotFound("Post","id",postId));
        Comment comment=mapper.map(commentDto,Comment.class);

        User  user=(userRepo.findById(userId).orElseThrow(()->new ResourceNotFound("User","id",userId)));

        comment.setPost(post);
        comment.setUser(user);

        return mapper.map(commentRepo.save(comment),CommentDto.class);
    }

    public void deleteComment(int id)
    {
        Comment comment= commentRepo.findById(id).orElseThrow(()->new ResourceNotFound("comment","id",id));

        commentRepo.delete(comment);
    }

    public List<CommentDto> getCommentsByPost(int postId) {
        List<Comment> comments = commentRepo.getCommentsByPost(postId);

        return comments.stream()
                .map(comment -> mapper.map(comment, CommentDto.class)) // Using ModelMapper for mapping
                .collect(Collectors.toList());
    }


    public CommentDto getCommentById(int postId, int commentId) {

        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Comment", "id", commentId));

        if (comment.getPost().getPostId()!=(post.getPostId())) {
            throw new ResourceNotFound("Comment", "postId", postId);
        }

        return mapper.map(comment, CommentDto.class);
    }
}
