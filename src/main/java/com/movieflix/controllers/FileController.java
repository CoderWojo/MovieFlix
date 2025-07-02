package com.movieflix.controllers;

import com.movieflix.service.FileService;
import com.sun.net.httpserver.Headers;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart(name = "file") MultipartFile file) throws IOException {
        String uploadedFileName = fileService.uploadFile(file);

        return ResponseEntity.ok("File uploaded: " + uploadedFileName);
    }

    @GetMapping("/{fileName}")
    public void serveFileHandler(@PathVariable String fileName, HttpServletResponse response) throws FileNotFoundException, IOException {
        InputStream resourceFile = fileService.getResourceFile(fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);

//        mówi przeglądarce jak ma potraktować dane
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename= \"" + fileName + "\"");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"dzikus.png\"");
        response.setHeader("X-Content-Type-Options", "nosniff");

        StreamUtils.copy(resourceFile, response.getOutputStream());
    }
}
