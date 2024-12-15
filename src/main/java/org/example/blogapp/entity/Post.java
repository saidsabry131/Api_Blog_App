package org.example.blogapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.blogapp.dto.PostDto;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private String title;
    @Column(length = 1000)
    private String content;
    @Column(length = 1000)
    private String imageName;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "categoryId")
    private Category category;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Comment> comments;


    public Post(PostDto postDto)
    {
        this.title=postDto.getTitle();
        this.content=postDto.getContent();
        this.date=postDto.getDate();
       // this.user=new User(postDto.getUser());
      //  this.category=new Category(postDto.getCategory());
        this.imageName=postDto.getImageName();
        //this.comments=postDto.getCommentSet();
    }



}
