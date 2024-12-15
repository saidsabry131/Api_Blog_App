package org.example.blogapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@SpringBootApplication
//@EnableSwagger2
public class BlogAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogAppApplication.class, args);
        System.out.println("done!!");
    }

   // @Bean
    CommandLineRunner lineRunner()
    {
        return ar->{
            uploadd();
        };
    }


    void upload() throws Exception {
        File file=new File("C:\\Users\\said sabry\\Pictures\\OIP.jpg");

        InputStream inputStream=new FileInputStream(file);

        OutputStream outputStream = new FileOutputStream(new File("src/main/resources/images/outimage.jpg"));



        int x;

        while((x=inputStream.read())!=-1)
        {
            outputStream.write(x);
        }

        inputStream.close();
        outputStream.flush();
        outputStream.close();

        System.out.println("donee");
    }


    void uploadd() throws Exception {
      //  String name=multipartFile.getName();

        String name="file.jpg";
        String path="C:\\Users\\said sabry\\Pictures\\OIP.jpg";
        String random= UUID.randomUUID().toString();
       String fileName=random.concat(name.substring(name.indexOf(".")));

       String filePath=path+"/"+fileName;
        System.out.println(filePath);

        File file=new File(path);


        InputStream inputStream=new FileInputStream(file);
        OutputStream outputStream=new FileOutputStream("src/main/resources/images/"+fileName);
        int x;
        while((x=inputStream.read())!=-1)
        {
            outputStream.write(x);
        }
       // Files.copy(inputStream, Path.of("src/main/resources/images/" + fileName));
    }


}
