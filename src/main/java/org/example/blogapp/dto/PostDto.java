package org.example.blogapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.blogapp.entity.Category;
import org.example.blogapp.entity.Comment;
import org.example.blogapp.entity.Post;
import org.example.blogapp.entity.User;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private String title;
    private String content;

    private String imageName;

    private Date date;

    private CategoryDto category;

    private UserDto user;

    private Set<String> commentSet=new HashSet<>();

    public PostDto(Post post)
    {
        this.title=post.getTitle();
        this.content=post.getContent();
        this.date=post.getDate();
        imageName= post.getImageName();
        category=new CategoryDto(post.getCategory());
        user=new UserDto(post.getUser());




    }

}
