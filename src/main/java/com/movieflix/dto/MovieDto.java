package com.movieflix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    @NotNull
    private Integer movieId;

    @NotBlank(message = "Please provide movie's title!")   // app lvl, works only on String type
    private String title;

    @NotBlank(message = "Please provide movie's director!")
    private String director;    // reżyser

    @NotBlank(message = "Please provide movie's studio!")
    private String studio;  // wytwórnia

    private Set<String> movieCast;  // obsada

    @NotNull(message = "Please provide movie's release year!")
    private Integer releaseYear;

    @NotBlank
    private String poster;

    @NotBlank
    private String posterURL;
}
