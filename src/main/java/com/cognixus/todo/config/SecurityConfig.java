package com.cognixus.todo.config;

import com.cognixus.todo.auth.CustomOncePerRequestFilter;
import com.cognixus.todo.auth.CustomUsernamePasswordAuthFilter;
import com.cognixus.todo.dto.Token;
import com.cognixus.todo.google.CognixusOAuth2User;
import com.cognixus.todo.google.CognixusOAuth2UserService;
import com.cognixus.todo.service.api.UserService;
import com.cognixus.todo.util.AuthUtil;
import com.cognixus.todo.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@Slf4j
public class SecurityConfig {

    @Autowired
    private CognixusOAuth2UserService oauthUserService;

    @Autowired
    private UserService userService;

    @Value("${security.jwt.token.signkey}")
    private String signKey;
    @Value("${security.jwt.token.expiry.minute:60}")
    private Integer tokenExpiryMinute;


    /**
     * Setup the http security
     *
     * @param http http security
     * @return security filter chain
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration
            , ObjectMapper mapper) {

        return http
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler(mapper))
                .and()
                //defined allowed API vs roles
                .authorizeRequests()
                //access
                .antMatchers(POST, "/user").hasAnyAuthority("Admin")
                .antMatchers(GET, "/user/name/**").hasAnyAuthority("Admin")
                .antMatchers(DELETE, "/user/name/**").hasAnyAuthority("Admin")
                .and()
                //google oauth2 login
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler((request, response, authentication) -> verifyGoogleLogin(response, authentication, mapper))
                .and()
                //disable csrf to avoid attack of cross-site
                .csrf().disable()
                //for h2-console UI display
                .headers().frameOptions().disable()
                //add filter to verify user login
                .and().addFilter(new CustomUsernamePasswordAuthFilter(authenticationConfiguration.getAuthenticationManager(), signKey, mapper, tokenExpiryMinute))
                //add filter to verify user login request is valid
                .addFilterBefore(new CustomOncePerRequestFilter(signKey, mapper), CustomUsernamePasswordAuthFilter.class).build()

                ;
    }

    @SneakyThrows
    public void verifyGoogleLogin(HttpServletResponse response
            , Authentication authentication, ObjectMapper mapper) {
        log.info("Authentication name: " + authentication.getName());

        if (((OAuth2AuthenticationToken) authentication).isAuthenticated()) {
            CognixusOAuth2User oauthUser = (CognixusOAuth2User) authentication.getPrincipal();

            userService.processOAuthPostLogin(oauthUser.getEmail());
            String accessToken = TokenUtil.generateToken(
                    authentication.getName(),
                    "congnixus",
                    List.of("Admin"),
                    signKey, tokenExpiryMinute
            );

            response.setContentType(APPLICATION_JSON_VALUE);
            response.getOutputStream().write(
                    mapper.writeValueAsBytes(Token.builder()
                            .accessToken(accessToken)
                            .build())
            );
        } else {
            AuthUtil.setAuthExceptionResponse(response, mapper, "failed authorization");

        }
    }

    /**
     * Override forbidden message
     *
     * @return access denied handler
     */
    private AccessDeniedHandler accessDeniedHandler(ObjectMapper mapper) {
        return (request, response, ex) -> AuthUtil.setAuthExceptionResponse(response, mapper, ex.getMessage());
    }

}
