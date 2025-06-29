package com.movieflix.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @NotBlank(message = "Please provide movie's title!")   // app lvl, works only on String type
    @Column(nullable = false, length = 200) // db lvl
    private String title;

    @NotBlank(message = "Please provide movie's director!")
    @Column(nullable = false)
    private String director;    // reżyser

    @NotBlank(message = "Please provide movie's studio!")
    @Column(nullable = false)
    private String studio;  // wytwórnia

    @CollectionTable(
            name = "movie_cast"
            // jeśli nie określę par. 'joincolumns to hibernate odnajdzie klucz obcy i nazwie go Movie_movie_id, name="NAZWA_KOLUMNY"
            , joinColumns = @JoinColumn(name = "movie_id", nullable = false)
            )
    @ElementCollection
    @Column(name = "actor_name", nullable = false)
    private Set<String> movieCast;  // obsada

    @NotNull(message = "Please provide movie's release year!")
    @Column(nullable = false)
    private Integer releaseYear;

    @NotBlank
    @Column(nullable = false)
    private String poster;
}
