package com.example.eCommerce.Dtos;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ExceptionDto {
    private HttpStatus status;
    private String message;
    private final LocalDateTime timeStamp = LocalDateTime.now();

    public ExceptionDto() {
    }

    public ExceptionDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}