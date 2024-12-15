package org.example.blogapp.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServices {

   public String uploadImage(String path, MultipartFile multipartFile) throws IOException {
       System.out.println("path is "+path);
        String name=multipartFile.getOriginalFilename();

        String fileName= UUID.randomUUID().toString().concat(name.substring(name.indexOf(".")));

        String fullPath=path+File.separator+fileName;

        File file=new File(path);

        if(!file.exists())
        {
            file.mkdir();
        }
            Files.copy(multipartFile.getInputStream(), Paths.get(fullPath));


        return fileName;
    }


    public InputStream getResources(String path,String fileName) throws FileNotFoundException {

        String fullPath=path+File.separator+fileName;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }


    public void getImagesAndResources(String path, HttpServletRequest request, HttpServletResponse response,String imageName) throws IOException {
        File file=new File(path+File.separator+imageName);

        if(!file.exists())
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("file not found"+imageName);
        }


        String contentType = request.getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream"; // Default to generic type if MIME is not found
        }

        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "inline; filename=\"" + imageName + "\"");


        InputStream streamRead=new FileInputStream(file);
        OutputStream streamWrite=response.getOutputStream();

        byte[] buffer=new byte[1024];
        int read;

        while((read=streamRead.read(buffer))!=-1)
        {
            streamWrite.write(buffer,0,read);
        }

        streamWrite.flush();
        streamRead.close();
        streamWrite.close();


    }


    public void downloadImage(String path,String imageName,HttpServletResponse response,HttpServletRequest request) throws Exception
    {
        File file = new File(path + File.separator + imageName);

        // Check if the file exists
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("File not found: " + imageName);
            return;
        }

        // Get the content type of the file
        String contentType = request.getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream"; // Default content type if not found
        }

        // Set the response content type
        response.setContentType(contentType);

        // Set Content-Disposition header to indicate download
        response.setHeader("Content-Disposition", "attachment; filename=\"" + imageName + "\"");

        // Read the file and write it to the response output stream
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            // Write file data to the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush(); // Make sure all data is written
        }
    }
}
