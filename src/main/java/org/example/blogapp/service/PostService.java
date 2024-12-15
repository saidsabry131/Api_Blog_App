package org.example.blogapp.service;

import org.example.blogapp.dto.PostDto;
import org.example.blogapp.entity.Category;
import org.example.blogapp.entity.Post;
import org.example.blogapp.entity.User;
import org.example.blogapp.exception.ResourceNotFound;
import org.example.blogapp.repository.CategoryRepo;
import org.example.blogapp.repository.PostRepo;
import org.example.blogapp.repository.UserRepo;
import org.example.blogapp.util.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    public PostService(PostRepo postRepo, UserRepo userRepo, CategoryRepo categoryRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    public PostDto createPost(PostDto postDto,
                    int userId,
                    int categoryId
    )
    {

        User user=userRepo.findById(userId)
                .orElseThrow(()->new ResourceNotFound("User","id",userId));


        Category category=categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFound("Category","id",categoryId));


        Post post =new Post(postDto);

        post.setDate(new Date());


        post.setCategory(category);
        post.setUser(user);

        return new PostDto(postRepo.save(post));
    }

    public List<Post> getAllPost()
    {
        return postRepo.findAll();
    }
    public List<PostDto> getAllByUser(int userId)
    {
        User user=userRepo.findById(userId)
                    .orElseThrow(()->new ResourceNotFound("User","id",userId));

        List<Post> postList=postRepo.getAllPostsByUser(user);

        List<PostDto> postDtoList=new ArrayList<>();

        for (Post post:postList)
        {
            postDtoList.add(new PostDto(post));
        }

        return postDtoList;
    }


    public List<PostDto> getAllByCategory(int  categoryId)
    {


        Category category=categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFound("Category", "id", categoryId));

       List<Post> posts= postRepo.getAllPostsByCategory(category);

       List<PostDto> postDtos=new ArrayList<>();

       for (Post post:posts)
       {
           postDtos.add(new PostDto(post));
       }



//      return posts.stream().map(PostDto::new)
//               .collect(Collectors.toList());

       return postDtos;
    }


    public List<PostDto> searchPost(String keyword)
    {

        List<Post> post=postRepo.search(keyword);
        return post.stream().map(PostDto::new).collect(Collectors.toList());
    }


    public PostResponse getAllPosts(int pageNumber, int pageSize,String sortBy)
    {

        Pageable pageable= PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));

        Page<Post> postPage= postRepo.findAll(pageable);

        List<Post> posts=postPage.getContent();

        List<PostDto> postDtos= posts.stream()
                .map(PostDto::new)
                .collect(Collectors.toList());


        return new PostResponse(postDtos,postPage.getNumber(),postPage.getSize(),postPage.getTotalElements(),postPage.getTotalPages(),postPage.isLast());

    }

    public PostDto getPostById(int id)
    {
        return new PostDto(postRepo.findById(id).orElseThrow(()->new ResourceNotFound("Post","id",id)));
    }

    public void deletePost(int id)
    {
        postRepo.delete(postRepo.findById(id).orElseThrow(()->new ResourceNotFound("Post","id",id)));
    }

    public PostDto updatePost(int id,PostDto  postDto)
    {
        Post post=postRepo.findById(id).orElseThrow(()->new ResourceNotFound("Post","id",id));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        postRepo.save(post);

        return new PostDto(post);
    }
}
