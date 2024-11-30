package com.onlinelearning.online_learning_platform.exception;

public class LessonNotFoundException extends RuntimeException{

    public LessonNotFoundException(String message) {
        super(message);
    }
}
