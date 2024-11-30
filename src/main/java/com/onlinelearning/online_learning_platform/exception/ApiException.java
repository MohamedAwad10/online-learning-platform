package com.onlinelearning.online_learning_platform.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {

    private HttpStatus status;
    private String message;
    private ZonedDateTime time;

    public ApiException(){}

    public ApiException(HttpStatus status, String message, ZonedDateTime time) {
        this.status = status;
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
}
