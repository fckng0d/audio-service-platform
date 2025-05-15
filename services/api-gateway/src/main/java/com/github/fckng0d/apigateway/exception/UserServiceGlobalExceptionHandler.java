package com.github.fckng0d.apigateway.exception;

import com.github.fckng0d.apigateway.dto.ApiError;
import com.github.fckng0d.apigateway.grpc.exception.authservice.EmailAlreadyExistsException;
import com.github.fckng0d.apigateway.grpc.exception.authservice.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestControllerAdvice
public class UserServiceGlobalExceptionHandler {

    @ExceptionHandler({ EmailAlreadyExistsException.class, UsernameAlreadyExistsException.class })
    public Mono<ResponseEntity<ApiError>> handleConflictExceptions(
            ServerWebExchange exchange, Throwable ex) {

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiError>> handleOtherExceptions(
            ServerWebExchange exchange, Throwable ex) {

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}