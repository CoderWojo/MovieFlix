package com.movieflix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieDto {

    private Integer id;

    @NotBlank(message = "Please provide movie's title!")   // app lvl, works only on String type
    private String title;

    @NotBlank(message = "Please provide movie's director!")
    private String director;    // reżyser

    @NotBlank(message = "Please provide movie's studio!")
    private String studio;  // wytwórnia

    private Set<String> movieCast;  // obsada

    @NotNull(message = "Please provide movie's release year!")
    private Integer releaseYear;

    private String posterFilename;

//    Gdy zmienimy sposób przechowywania plakatów, to wystarczy zmienić MovieDto a encję pozostawić bez zmian
    private String posterURL;
}