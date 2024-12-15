package org.example.blogapp.repository;

import org.example.blogapp.dto.PostDto;
import org.example.blogapp.entity.Category;
import org.example.blogapp.entity.Post;
import org.example.blogapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepo  extends JpaRepository<Post,Integer> {

    List<Post> findAllByUser(User user);

    List<Post> findAllByCategory(Category category);

    @Query("select p from Post p join p.user u where u=:user")
    List<Post> getAllPostsByUser(@Param("user") User user);


    @Query("select p from Post p join p.category c where c=:category")
    List<Post> getAllPostsByCategory(Category category);

    @Query("select p from Post p where p.content like %:title%")
    List<Post> search(@Param("title") String title);

    List<Post> findByContentContaining(String title);
}
