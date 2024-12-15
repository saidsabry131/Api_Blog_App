package org.example.blogapp.repository;

import org.example.blogapp.dto.CommentDto;
import org.example.blogapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepo  extends JpaRepository<Comment,Integer> {

    @Query("select c from  Comment c join c.post where c.post.postId=:postId ")
    List<Comment> getCommentsByPost(@Param("postId") int postId);
}
