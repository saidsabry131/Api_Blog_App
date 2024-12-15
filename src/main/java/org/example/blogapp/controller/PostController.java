package org.example.blogapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.blogapp.dto.PostDto;
import org.example.blogapp.entity.Post;
import org.example.blogapp.exception.ApiResponse;
import org.example.blogapp.service.FileServices;
import org.example.blogapp.service.PostService;
import org.example.blogapp.util.PostResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PostController {

    @Value("${project.image}")
    private  String path;

    private final PostService postService;

    private final FileServices fileServices;

    public PostController(PostService postService, FileServices fileServices) {
        this.postService = postService;
        this.fileServices = fileServices;
    }

    @PostMapping("/user/{userId}/category/{categoryId}/posts/")
    public ResponseEntity<PostDto> createPost(
            @RequestPart("postDto") String postDtoJson, // Receive JSON as a string
            @PathVariable int userId,
            @PathVariable int categoryId,
            @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile
    ) throws IOException {
        // Deserialize JSON to PostDto
        ObjectMapper objectMapper = new ObjectMapper();
        PostDto postDto = objectMapper.readValue(postDtoJson, PostDto.class);

        // Handle file upload
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String name = fileServices.uploadImage(path, multipartFile);
            postDto.setImageName(name);
        }

        System.out.println("Content-Type: " + (multipartFile != null ? multipartFile.getContentType() : "null"));
        System.out.println("File Name: " + (multipartFile != null ? multipartFile.getOriginalFilename() : "null"));
        System.out.println("Post DTO: " + postDto);

        PostDto createdPost = postService.createPost(postDto, userId, categoryId);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }
// the current user can insert not using user id for adding post
//    @PostMapping("/user/category/{categoryId}/posts/")
//    public ResponseEntity<PostDto> createPostt(
//            @RequestPart("postDto") String postDtoJson, // Receive JSON as a string
////            @PathVariable int userId,
//            @AuthenticationPrincipal UserDetails userDetails,
//            @PathVariable int categoryId,
//            @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile
//    ) throws IOException {
//        // Deserialize JSON to PostDto
//        ObjectMapper objectMapper = new ObjectMapper();
//        PostDto postDto = objectMapper.readValue(postDtoJson, PostDto.class);
//
//        // Handle file upload
//        if (multipartFile != null && !multipartFile.isEmpty()) {
//            String name = fileServices.uploadImage(path, multipartFile);
//            postDto.setImageName(name);
//        }
//
//        System.out.println("Content-Type: " + (multipartFile != null ? multipartFile.getContentType() : "null"));
//        System.out.println("File Name: " + (multipartFile != null ? multipartFile.getOriginalFilename() : "null"));
//        System.out.println("Post DTO: " + postDto);
//
//        PostDto createdPost = postService.createPost(postDto, userId, categoryId);
//
//        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
//    }





    @GetMapping("/user/{userId}/posts/")
    public ResponseEntity<List<PostDto>> getByUser(@PathVariable int userId)
    {
       return new ResponseEntity<>(postService.getAllByUser(userId),HttpStatus.OK) ;
    }


    @GetMapping("/category/{categoryId}/posts/")
    public ResponseEntity<List<PostDto>> getByCategory(@PathVariable int categoryId)
    {
        return new ResponseEntity<>(postService.getAllByCategory(categoryId),HttpStatus.OK) ;
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "PageNumber",defaultValue = "10",required = false) int pageNumber,
            @RequestParam(value = "PageSize",defaultValue = "1",required = false) int pageSize,
            @RequestParam(value = "SortBy" ,defaultValue = "postId",required = false) String sortBy
     )
    {

        return new ResponseEntity<>(postService.getAllPosts(pageNumber,pageSize,sortBy),HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") int postId)
    {

        return new ResponseEntity<>(postService.getPostById(postId),HttpStatus.FOUND);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable int id,@RequestBody PostDto postDto)
    {
        return new ResponseEntity<>(postService.updatePost(id,postDto),HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable int id)
    {
        postService.deletePost(id);
        return new ResponseEntity<>(new ApiResponse(true,"post deleted"),HttpStatus.OK);
    }

    @GetMapping("/posts/search/{keyword}")

    public ResponseEntity<List<PostDto>> search(@PathVariable String keyword)
    {
        return new ResponseEntity<>(postService.searchPost(keyword),HttpStatus.OK);
    }

    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto>  upload(
            @RequestParam("imageFile")MultipartFile imageFile,
            @PathVariable int postId

    )
    {
        String fileName;
        PostDto postDto1;
        try {
            fileName=fileServices.uploadImage(path,imageFile);
            PostDto postDto=postService.getPostById(postId);
            postDto.setImageName(fileName);

           postDto1= postService.updatePost(postId,postDto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(postDto1,HttpStatus.OK);
    }


    @GetMapping(value = "/images/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void getResources(
            @PathVariable String imageName,
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {
      // fileServices.getImagesAndResources(path,request,response,imageName);

        InputStream inputStream=fileServices.getResources(path,imageName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);

        StreamUtils.copy(inputStream,response.getOutputStream());

    }
}
