package com.movieflix.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movieflix.dto.MovieDto;
import com.movieflix.service.MovieService;
import com.movieflix.mapper.MovieMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart("movie") String strDto,
                                                    @RequestPart("file") MultipartFile file) throws JsonProcessingException, IOException {
        MovieDto dto = movieMapper.strToDto(strDto);
        MovieDto savedDto = movieService.addMovie(dto, file);

        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
        return ResponseEntity.ok(movieService.getAll());
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMoviehandler(@PathVariable Integer movieId,
                                                       @RequestPart("movie") String movieDto,
                                                       @RequestPart("file") MultipartFile file) throws JsonProcessingException, IOException {
        MovieDto dto = movieMapper.strToDto(movieDto);
        return ResponseEntity.ok(movieService.updateMovie(movieId, dto, file));
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(movieId));
    }
}
