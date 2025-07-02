package com.movieflix.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@Service
public class FileServiceImpl implements FileService {

    @Value("${project.posters.path}")
    private String posters_path;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {

        // get original file name
        String filename = file.getOriginalFilename();

        if(Files.exists(Paths.get(posters_path, filename))) {
            throw new FileAlreadyExistsException("A file with the same name already exists! Please change the name.");
        }

        if(filename == null || filename.isBlank()) {
            throw new FileNotFoundException("The file does not have a name.");
        }

        // check if exists
        Path uploadDir = Paths.get(posters_path);
        if(Files.notExists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }

        Files.copy(file.getInputStream(), uploadDir.resolve(filename));

        return filename;
    }

    @Override
    public InputStream getResourceFile(String filename) throws FileNotFoundException {
        Path readDir = Paths.get(posters_path).resolve(filename);

        if(Files.notExists(readDir)) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        return new FileInputStream(readDir.toFile());
    }
}
