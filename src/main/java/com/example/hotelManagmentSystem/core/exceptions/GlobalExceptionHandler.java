package com.example.hotelManagmentSystem.core.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResourceException> handleResourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException
    ){
        ResourceException resourceException = ResourceException.builder()
                .message(resourceNotFoundException.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .throwable(resourceNotFoundException)
                .build();

        return new ResponseEntity<>(resourceException,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRoomPriceException.class)
    public ResponseEntity<InputFormatException> handleInvalidRoomPriceException(
            InvalidRoomPriceException resourceNotFoundException
    ){
        InputFormatException inputFormatException = InputFormatException.builder()
                .message(resourceNotFoundException.getMessage()).build();

        return new ResponseEntity<>(inputFormatException,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<TokenException> handleExpiredJwtException(
            ExpiredJwtException expiredJwtException
    ){
        TokenException tokenException = TokenException.builder()
                .message(expiredJwtException.getMessage())
                .build();

        return new ResponseEntity<>(tokenException,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<TokenException> handleInvalidTokenException(
            InvalidTokenException invalidTokenException
    ){
        TokenException tokenException = TokenException.builder()
                .message(invalidTokenException.getMessage())
                .build();

        return new ResponseEntity<>(tokenException,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ResourceException> handleInvalidRequestException(
            InvalidRequestException invalidRequestException
    ){
        ResourceException resourceException = ResourceException.builder()
                .message(invalidRequestException.getMessage())
                .build();

        return new ResponseEntity<>(resourceException,HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(BookingException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ResourceException> handleBookingRequestException(
            BookingException bookingException
    ){
        ResourceException resourceException = ResourceException.builder()
                .message(bookingException.getMessage())
                .build();

        return new ResponseEntity<>(resourceException,HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UploadImageException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ResourceException> handleUploadImageException(
           UploadImageException uploadImageException
    ){
        ResourceException resourceException = ResourceException.builder()
                .message(uploadImageException.getMessage())
                .build();

        return new ResponseEntity<>(resourceException,HttpStatus.NOT_ACCEPTABLE);
    }



}
