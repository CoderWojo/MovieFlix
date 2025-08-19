package com.movieflix.service;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    List<MovieDto> getThreeMovies();

//    1. bez posterFilename oraz posterURL oraz id bo ono zostanie dodane automatycznie przez db
    MovieDto addMovie(MovieDto movieRequestDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer id);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer id, MovieDto movieDto, MultipartFile file) throws IOException;

    String deleteMovie(Integer id) throws IOException;

    MoviePageResponse getAllMoviesWithPagination(String page, String pageSize);

    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String dir);
}
