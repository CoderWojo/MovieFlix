package com.movieflix.service;

import com.movieflix.exception.PosterNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadFile(MultipartFile file) throws IOException;

    InputStream getResourceFile(String filename) throws FileNotFoundException;
}
