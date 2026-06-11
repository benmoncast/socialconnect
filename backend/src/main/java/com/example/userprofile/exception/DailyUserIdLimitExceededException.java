package com.example.userprofile.exception;

public class DailyUserIdLimitExceededException extends RuntimeException {

    public DailyUserIdLimitExceededException(String message) {
        super(message);
    }
}
