package org.example.blogapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.blogapp.entity.Comment;
import org.example.blogapp.entity.Post;
import org.example.blogapp.entity.User;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private int id;
    private String content;
    private PostDto post;
    private UserDto user;


}
