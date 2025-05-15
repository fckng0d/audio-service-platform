package com.github.fckng0d.authenticationservice.exception.grpc.userservice;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class UserServiceExceptionHandler {

    private static final Map<Status.Code, Function<StatusRuntimeException, RuntimeException>> exceptionHandlers = Map.of(
            Status.Code.ALREADY_EXISTS, e -> {
                String message = e.getStatus().getDescription();
                if (message != null && message.toLowerCase().contains("email")) {
                    return new EmailAlreadyExistsException(message, e);
                } else if (message != null && message.toLowerCase().contains("username")) {
                    return new UsernameAlreadyExistsException(message, e);
                }
                return new RuntimeException(message, e);
            },
            Status.Code.INTERNAL, e -> new RuntimeException("Internal server error in user-service: " + e.getStatus().getDescription(), e)
    );

    public RuntimeException handle(StatusRuntimeException e) {
        Function<StatusRuntimeException, RuntimeException> handler = exceptionHandlers.get(e.getStatus().getCode());
        if (handler != null) {
            return handler.apply(e);
        } else {
            return new RuntimeException("Необработанная ошибка сервиса user-service: " + e.getStatus().getDescription(), e);
        }
    }
}

