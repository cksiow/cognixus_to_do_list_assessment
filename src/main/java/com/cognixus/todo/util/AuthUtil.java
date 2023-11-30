package com.cognixus.todo.util;

import com.cognixus.todo.exception.ErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class AuthUtil {

    private AuthUtil() {

    }

    @SneakyThrows
    public static void setAuthExceptionResponse(HttpServletResponse response, ObjectMapper mapper, String message) {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(
                mapper.writeValueAsBytes(ErrorHandler.builder().error(message).build())
        )
        ;
    }
}
