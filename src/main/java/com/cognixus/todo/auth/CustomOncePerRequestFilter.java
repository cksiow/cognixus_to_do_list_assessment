package com.cognixus.todo.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cognixus.todo.constant.Constant;
import com.cognixus.todo.util.AuthUtil;
import com.cognixus.todo.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter {
    private final List<String> permitAllPaths = Arrays.asList
            (
                    "/login.*", "/user/token/refresh", "/h2-console.*", "/favicon.ico",
                    "/swagger-ui/.*", "/v\\d/api-docs/.*", "//oauth.*"
            );
    private final String signKey;
    private final ObjectMapper mapper;

    public CustomOncePerRequestFilter(String signKey, ObjectMapper mapper) {
        this.signKey = signKey;
        this.mapper = mapper;
    }

    /**
     * filter the user request
     *
     * @param request     request info
     * @param response    response info
     * @param filterChain filter chain
     */
    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        log.info("request filter checking: {}", request.getRequestURI());
        //check the request url, if it is whitelist, by pass auth checking, otherwise check the token
        if (permitAllPaths.stream().anyMatch(p -> request.getRequestURI().matches(p))) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.matches(Constant.BEARER_REGEX)) {
                DecodedJWT decodedJWT = null;
                try {
                    decodedJWT = TokenUtil.verifyToken(authorizationHeader, signKey);
                    //get the username and roles + pass into security context holder
                    String username = decodedJWT.getSubject();
                    var roles = decodedJWT.getClaim("roles").asList(String.class);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request, response);
                } catch (Exception ex) {
                    //token invalid
                    log.error("doFilterInternal exception: {}", ex.getMessage());
                    AuthUtil.setAuthExceptionResponse(response, mapper, ex.getMessage());
                }
            } else {
                //if the passing token format invalid
                AuthUtil.setAuthExceptionResponse(response, mapper, "Invalid token");
            }
        }
    }
}
