package com.cognixus.todo.util;

import com.cognixus.todo.audit.Audit;
import com.cognixus.todo.exception.ErrorHandler;
import com.cognixus.todo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    public static Long getLoginId() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        }
        return null;
    }

    public static void assignCreatedBy(List<? extends Audit> list) {
        var loginId = getLoginId();
        for (Audit audit : list) {
            if (audit.getCreatedBy() == null) {
                audit.setCreatedBy(loginId);
            }
        }
    }
}
