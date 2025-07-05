package com.movieflix.dto;

import java.util.List;

public record MoviePageResponse(
        List<MovieDto> movieDtos,
        Integer pageNumber,
        Integer pageSize,   // records per one page
        Integer totalElements,
        Integer totalPages,
        boolean isLast
) {
}
