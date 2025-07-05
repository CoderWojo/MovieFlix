package com.movieflix.service;

import com.movieflix.exception.EmptyFileException;
import com.movieflix.exception.PosterNotFoundException;
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

        // check if poster exists
        if(Files.exists(Paths.get(posters_path, filename))) {
            throw new FileAlreadyExistsException("A file with the same name already exists! Please change the name.");
        }

//        check if file is not empty
        if(filename == null || filename.isBlank()) {
            throw new EmptyFileException("The file: " + file.getOriginalFilename() + " seems to be empty!");
        }

        // check if posters folder exists - create?
        Path uploadDir = Paths.get(posters_path);
        if(Files.notExists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }

//        dodaj poster do /posters
        Files.copy(file.getInputStream(), uploadDir.resolve(filename));

        return filename;
    }

    @Override
    public InputStream getResourceFile(String filename) throws FileNotFoundException {
        Path readDir = Paths.get(posters_path).resolve(filename);

        if(Files.notExists(readDir)) {
            throw new FileNotFoundException("Poster with name: " + filename + " does not exist!");
        }


        return new FileInputStream(readDir.toFile());
    }
}
