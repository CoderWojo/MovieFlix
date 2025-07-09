package com.movieflix.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;

// Każde wyłapanie wyjątku przez GlobalExceptionHandler spowoduje że nie wycieknie dalej do domyślnego mechanizmu Springa czyli np na terminal
@RestControllerAdvice
public class GlobalExceptionHandler {

//    Slf4j nie loguje, tylko przekazuje logi dalej do prawdziwego systemu logowania np Logback
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
//            FileAlreadyExistsException.class,
            FileNotFoundException.class,
            EmptyFileException.class,
            IOException.class,
            BadRequestException.class,
            MovieNotFoundException.class
    })
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex, HttpServletRequest request) {
        HttpStatus status;

        if(ex instanceof FileAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else if(ex instanceof FileNotFoundException || ex instanceof MovieNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof EmptyFileException || ex instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

//        Przesyłamy na końcu obiekt 'ex' wyjątku po to aby w terminalu był wyświetlony pełny stack-trace
//        w miejsca {} {} zostaną umieszczone kolejne argumenty po 'message'
        logger.error("Obsługa wyjątku: {} (status {})", ex.getClass().getName(), status.toString(), ex);

        ApiError body = new ApiError(
                LocalDateTime.now(),
                status.toString(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public ProblemDetail handleFileAlreadyExistsException(FileAlreadyExistsException ex, HttpServletRequest request) throws URISyntaxException {
//        Spring + Jackson automatycznie wrzucą te pola do JSON'a jako top-level (czyli na głównym poziomie)
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
//        problem.setStatus(HttpStatus.CONFLICT.value());
        problem.setDetail(ex.getMessage());
        problem.setInstance(new URI(request.getRequestURI()));  // path żądania które spowodowało wyjatek
        problem.setTitle(ex.getClass().getSimpleName());
        problem.setType(URI.create(request.getRequestURI()));   // link gdzie można poczytać więcej o tym problemie

        return problem;
    }
}
