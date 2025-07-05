package com.movieflix.controllers;

import com.movieflix.service.FileService;
import com.sun.net.httpserver.Headers;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
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
    public ResponseEntity<InputStreamResource> serveFileHandler(@PathVariable String fileName, HttpServletResponse response) throws FileNotFoundException, IOException {
//        pobierz z warstwy serwisowej strumień do odczytu postera
        InputStream resourceFile = fileService.getResourceFile(fileName);

        InputStreamResource body = new InputStreamResource(resourceFile);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(body);
    }
}
