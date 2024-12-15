package org.example.blogapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URISyntaxException;


@SpringBootTest
class BlogappApplicationTests {

   // @Test
    void contextLoads() throws URISyntaxException {
        System.out.println("mypath is : "+getClass().getClassLoader().getResource(""));

    }

//    @Test


}
