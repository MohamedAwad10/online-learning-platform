package com.onlinelearning.online_learning_platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

//        Map<String, String> errorMap = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errorMap.put(error.getField(), error.getDefaultMessage()));

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                badRequest,
                ex.getFieldError().getDefaultMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<?> handleCategoryException(CategoryException ex){

        HttpStatus conflict = HttpStatus.CONFLICT;
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                ex.getMessage().equals("Category not found") ? notFound : conflict,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, ex.getMessage().equals("Category not found") ? notFound : conflict);
    }

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<?> handleCourseException(CourseException ex){

        HttpStatus conflict = HttpStatus.CONFLICT;
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                ex.getMessage().equals("Course not found") ? notFound : conflict,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, ex.getMessage().equals("Course not found") ? notFound : conflict);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){

        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(commonCode(ex, notFound), notFound);
    }

    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<?> handleEnrollmentException(EnrollmentException ex){

        HttpStatus conflict = HttpStatus.CONFLICT;
        return new ResponseEntity<>(commonCode(ex, conflict), conflict);
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<?> handleLessonNotFoundException(LessonNotFoundException ex){

        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(commonCode(ex, notFound), notFound);
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<?> handleReviewException(ReviewException ex){

        HttpStatus notFound = HttpStatus.NOT_FOUND;
        HttpStatus conflict = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                ex.getMessage().equals("Review not found") ? notFound : conflict,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, ex.getMessage().equals("Review not found") ? notFound : conflict);
    }

    @ExceptionHandler(TagException.class)
    public ResponseEntity<?> handleTagException(TagException ex){

        HttpStatus notFound = HttpStatus.NOT_FOUND;
        HttpStatus conflict = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                ex.getMessage().equals("Tag not found") ? notFound : conflict,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, ex.getMessage().equals("Tag not found") ? notFound : conflict);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<?> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex){

        HttpStatus conflict = HttpStatus.CONFLICT;
        return new ResponseEntity<>(commonCode(ex, conflict), conflict);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException ex){

        HttpStatus notFound = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(commonCode(ex, notFound), notFound);
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<?> handleRoleException(RoleException ex){

        HttpStatus conflict = HttpStatus.CONFLICT;
        return new ResponseEntity<>(commonCode(ex, conflict), conflict);
    }

    public ApiException commonCode(Exception ex, HttpStatus status){

        ApiException apiException = new ApiException(
                status,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return apiException;
    }
}