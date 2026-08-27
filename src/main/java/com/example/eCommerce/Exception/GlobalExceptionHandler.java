package com.example.eCommerce.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.eCommerce.Dtos.ExceptionDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> UsernameNotFoundException(UsernameNotFoundException ex) {
        ExceptionDto response = new ExceptionDto();
        response.setMessage(ex.getMessage());
        response.setStatus((HttpStatus.NOT_FOUND));
    
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ExceptionDto> UserExistsHandler(UserExistsException Ex) {

        ExceptionDto response = new ExceptionDto();
        response.setMessage(Ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> IllegalArgumentHandler(IllegalArgumentException Ex) {

        ExceptionDto response = new ExceptionDto();
        response.setMessage(Ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionDto> IllegalStateException(IllegalStateException Ex) {
        
        ExceptionDto response = new ExceptionDto();
        response.setMessage(Ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    
    
    @ExceptionHandler(RuntimeExceptions.class)
    public ResponseEntity<ExceptionDto> RuntimeExceptions(RuntimeExceptions Ex) {
        
        ExceptionDto response = new ExceptionDto();
        response.setMessage(Ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> AllExceptionHandler(Exception ex) {
        
        ExceptionDto response = new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        
    }

}
