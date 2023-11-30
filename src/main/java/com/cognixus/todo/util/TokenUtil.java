package com.cognixus.todo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cognixus.todo.constant.Constant;

import java.util.Date;
import java.util.List;

public class TokenUtil {

    private TokenUtil() {

    }

    /**
     * Verify the token is valid or not
     *
     * @param token   jwt token
     * @param signKey signKey to verify
     * @return decoded jwt token info
     */

    public static DecodedJWT verifyToken(String token, String signKey) {
        if (token.matches(Constant.BEARER_REGEX)) {
            //remove Bearer
            token = token.substring("Bearer ".length());
        }
        Algorithm algorithm = Algorithm.HMAC256(signKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        //verify with sign key
        return verifier.verify(token);
    }

    /**
     * generate the token based on the passing information
     *
     * @param username          user name
     * @param id                user id
     * @param issuer            issuer that belong to which coming source
     * @param roles             user roles
     * @param signKey           sign key
     * @param tokenExpiryMinute token expiry in minute
     * @return jwt token
     */
    public static String generateToken(String username, Long id, String issuer, List<String> roles, String signKey, Integer tokenExpiryMinute) {
        Algorithm algorithm = Algorithm.HMAC256(signKey.getBytes());
        //create token with sign key
        return JWT.create()
                .withSubject(username)
                .withClaim("id", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + (tokenExpiryMinute * 60 * 1000)))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
    }
}
