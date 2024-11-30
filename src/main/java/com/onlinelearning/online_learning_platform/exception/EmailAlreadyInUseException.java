package com.onlinelearning.online_learning_platform.exception;

public class EmailAlreadyInUseException extends RuntimeException{

    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
