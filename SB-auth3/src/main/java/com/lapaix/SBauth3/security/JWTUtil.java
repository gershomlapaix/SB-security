package com.lapaix.SBauth3.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * By Gershom
 *
 * This is the class for creation and verification of the JWTs
 * Dependency used : "java-jwt" v3.19.2
 * */

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;

/** for token creation */
    public String generateToken(String email) throws IllegalArgumentException, JWTCreationException{
         return JWT.create()
                 .withSubject("User details")
                 .withClaim("email", email)
                 .withIssuedAt(new Date())
                 .withIssuer("La paix")
                 .sign(Algorithm.HMAC256(secret));
    }

    /** for token validation */
    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("La paix")
                .build();

        DecodedJWT jwt = verifier.verify(token);  // verify the token
        return jwt.getClaim("email").asString();  // return the email from claims
    }
}
