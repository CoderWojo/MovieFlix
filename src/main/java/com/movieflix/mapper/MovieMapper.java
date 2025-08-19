package com.movieflix.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.dto.MovieDto;
import com.movieflix.entities.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    private ObjectMapper mapper;

    public MovieMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Movie toMovie(MovieDto dto) {
        Movie m = new Movie();
        m.setTitle(dto.getTitle());
        m.setDirector(dto.getDirector());
        m.setStudio(dto.getStudio());
        m.setMovieCast(dto.getMovieCast());
        m.setReleaseYear(dto.getReleaseYear());
        m.setPosterFilename(dto.getPosterFilename());

        return m;
    }

    public MovieDto movieToDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDirector(movie.getDirector());
        dto.setStudio(movie.getStudio());
        dto.setMovieCast(movie.getMovieCast());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setPosterFilename(movie.getPosterFilename());
        dto.setPosterURL("http://localhost:8080/file/" + movie.getPosterFilename());

        return dto;
    }

    public MovieDto strToDto(String str) throws JsonProcessingException {
        return mapper.readValue(str, MovieDto.class);
    }
}
