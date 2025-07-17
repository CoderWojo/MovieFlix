package com.movieflix.service;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.entities.Movie;
import com.movieflix.exceptions.MovieNotFoundException;
import com.movieflix.repository.MovieRepository;
import com.movieflix.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final MovieMapper movieMapper;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${project.posters.path}")
    private String postersPath;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService, MovieMapper mapper) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.movieMapper = mapper;
    }

//    pozwalamy na nieuzupełnienie posterFilename w json ale wymagamy aby w RequestPart ten poster był obecny
    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // 1. upload a file
        String posterFilename = fileService.uploadFile(file);

        // 2. set value of 'posterFilename' field in dto
        movieDto.setPosterFilename(posterFilename);

        // 3. map dto to Movie object
        Movie movie = movieMapper.toMovie(movieDto);

        // 4. save the Movie object -> saved Movie object
        Movie savedMovie = movieRepository.save(movie);

        // 5. set 'posterURL' in dto object
        String posterUrl = baseUrl + savedMovie.getPosterFilename();

        // 6. map movie object to movieDto and return it
        MovieDto response = movieMapper.movieToDto(savedMovie);
        response.setPosterURL(posterUrl);

        return response;
    }

    @Override
    public MovieDto getMovie(Integer id) {
        // 1. check the data in DB if exists, fetch the data of given id
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id)); // NoSuchElementException

        // 2. Generate MovieDto using mapper
        MovieDto dto = movieMapper.movieToDto(movie);

        // 3. set posterURL
        String url = baseUrl + dto.getPosterFilename();
        dto.setPosterURL(url);

        return dto;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        // 1. fetch all movies from db
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        // 2. iterate through the list and generate URL's for each movie objects
        for(Movie m : movies) {
            String url = baseUrl + m.getPosterFilename();
            MovieDto dto = movieMapper.movieToDto(m);
            dto.setPosterURL(url);

            // 3. map to MovieDTO
            movieDtos.add(dto);
        }

        return movieDtos;
    }

    @Override
    @Transactional
    public MovieDto updateMovie(Integer id, MovieDto movieDto, MultipartFile file) throws IOException {
        // check if movie with given id already exists
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
        String filename = movie.getPosterFilename();

        Movie newMovie = movieMapper.toMovie(movieDto);
        newMovie.setId(movie.getId());

        String newPosterUrl = null;
        if (file != null && !file.isEmpty()) {
            // update poster field
            newMovie.setPosterFilename(file.getOriginalFilename());
            newPosterUrl = baseUrl + file.getOriginalFilename();
            // if file is not null, then delete existing fil associated with the record
            // and upload the new file
            Files.deleteIfExists(Paths.get(postersPath, filename));
            fileService.uploadFile(file);
        } else {
            newMovie.setPosterFilename(movie.getPosterFilename());
        }
        // if file is null, do nothing
        Movie updatedMovie = movieRepository.save(newMovie);
        // save the Movie object -> generate posterURL (new)^^? and return dto based


        // convert savedMovie to movieDto
        MovieDto response = movieMapper.movieToDto(updatedMovie);
        if(newPosterUrl != null) {
            response.setPosterURL(newPosterUrl);
        }
        else {
            response.setPosterURL(baseUrl + filename);
        }

        return response;
    }

    @Override
    public String deleteMovie(Integer id) throws IOException {
        // check if movie object exists in DB
        Movie mv = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));

        String posterName = mv.getPosterFilename();
        Integer mvId = mv.getId();  // dobrze pobeirac dane z db a nie od usera

        //  delete the file associated with
        Files.deleteIfExists(Paths.get(postersPath, posterName));

        // delete movie
        movieRepository.delete(mv);

        return "Movie deleted with id = " + mvId;
    }


    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);   // getTotalPages, getTotalElements
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = movies.stream()
                .map(movie -> {
                    MovieDto dto = movieMapper.movieToDto(movie);
                    dto.setPosterURL(baseUrl + movie.getPosterFilename());
                    return dto;
                })
                .collect(Collectors.toList());

        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                (int)moviePages.getTotalElements(), moviePages.getTotalPages(), moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize,
                                                                  String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<MovieDto> movieDtos = moviePages.getContent()
                .stream()
                .map(movie -> {
                    MovieDto dto = movieMapper.movieToDto(movie);
                    dto.setPosterURL(baseUrl + movie.getPosterFilename());
                    return dto;
                })
                .collect(Collectors.toList());

        return new MoviePageResponse(movieDtos, pageNumber, pageSize,
                (int)moviePages.getTotalElements(), moviePages.getTotalPages(), moviePages.isLast());
    }
}
