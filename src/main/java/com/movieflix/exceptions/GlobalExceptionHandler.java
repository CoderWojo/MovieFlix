package com.movieflix.exceptions;

import com.movieflix.auth.exceptions.RefreshTokenOutOfDateException;
import com.movieflix.auth.exceptions.UserAlreadyExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

// Każde wyłapanie wyjątku przez GlobalExceptionHandler spowoduje że nie wycieknie dalej do domyślnego mechanizmu Springa czyli np na terminal
@RestControllerAdvice
public class GlobalExceptionHandler {

//    Slf4j nie loguje, tylko przekazuje logi dalej do prawdziwego systemu logowania np Logback
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
//            FileAlreadyExistsException.class,
            FileNotFoundException.class,
            EmptyFileException.class,
            IOException.class,
            BadRequestException.class,
            MovieNotFoundException.class,
            UserAlreadyExistsException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            RefreshTokenOutOfDateException.class,
            UserNotFoundException.class,
            InvalidVerificationCodeException.class,
            NotTheSamePasswordException.class,
            ForgotPasswordNotFound.class,
            ExpiredOtpException.class,
            ExpectedForgotPasswordNotFound.class,
            ConstraintViolationException.class,
            UserNotAuthenticated.class,
            NotTheSameOldPasswordException.class,
            NewPasswordTheSameAsOldException.class,
            NotTheSameOtpException.class,
            RepeatedPasswordNotTheSameAsNew.class,
            CodeAlreadyUsedException.class,
            TooManyAttemptsException.class,
            AccessDeniedException.class,
            FileAlreadyExistsException.class,
            ObjectOptimisticLockingFailureException.class
    })
    public ProblemDetail handleAllExceptions(Exception ex, HttpServletRequest request) {
        HttpStatus status = getHttpStatus(ex);

//        Przesyłamy na końcu obiekt 'ex' wyjątku po to aby w terminalu był wyświetlony pełny stack-trace
//        w miejsca {} {} zostaną umieszczone kolejne argumenty po 'message'
        logger.error("Obsługa wyjątku: {} (status {})", ex.getClass().getName(), status.toString(), ex);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(ex.getClass().getSimpleName()); // info dla debugowania
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getRequestURI())); // ang. przypadek

        problem.setProperty("timestamp", LocalDateTime.now());
//        problem.setType(); // TODO: przekieruj do osobnej dokumentacji do tego typu błędu

        return problem;
    }

    private static HttpStatus getHttpStatus(Exception ex) {
        HttpStatus status;

//        ExpiredJwtException jest rzucany w filtrze w metodzie jwtService.extractUsername(jwt); (extractAllclaims) i nie dochodzi do warstwy kontrolerów z której to właśnie byłby wyłapany przez ten nasz Handler
        if(ex instanceof FileAlreadyExistsException || ex instanceof UserAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
        } else if(ex instanceof ExpectedForgotPasswordNotFound || ex instanceof ForgotPasswordNotFound || ex instanceof UserNotFoundException || ex instanceof FileNotFoundException || ex instanceof MovieNotFoundException || ex instanceof UsernameNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof ObjectOptimisticLockingFailureException || ex instanceof MethodArgumentNotValidException || ex instanceof TooManyAttemptsException || ex instanceof CodeAlreadyUsedException || ex instanceof RepeatedPasswordNotTheSameAsNew || ex instanceof NotTheSameOtpException || ex instanceof NewPasswordTheSameAsOldException || ex instanceof NotTheSameOldPasswordException || ex instanceof UserNotAuthenticated || ex instanceof ConstraintViolationException || ex instanceof ExpiredOtpException || ex instanceof NotTheSamePasswordException || ex instanceof InvalidVerificationCodeException || ex instanceof EmptyFileException || ex instanceof RefreshTokenOutOfDateException || ex instanceof BadRequestException || ex instanceof BadCredentialsException) {
            status = HttpStatus.BAD_REQUEST;
        } else if(ex instanceof AccessDeniedException) {
          status = HttpStatus.FORBIDDEN;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        HttpStatus status = getHttpStatus(ex);
        ProblemDetail problem = ProblemDetail.forStatus(status);

        problem.setTitle(ex.getClass().getSimpleName());
        problem.setDetail(cutTheMessage(ex)); // skróć
        problem.setInstance(URI.create(request.getRequestURI()));

        problem.setProperty("timestamp", LocalDateTime.now());

        return problem;
    }

    private String cutTheMessage(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        return fieldError.getDefaultMessage();
    }
}
