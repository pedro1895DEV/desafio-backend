package com.pedromartins.backend_challenge.services;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pedromartins.backend_challenge.models.Email;
import com.pedromartins.backend_challenge.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.JWT;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JwtTokenService {


    @Value("${api.secret.token.SECRET_KEY:default-secret-key}")
    private String SECRET_KEY;

    private String ISSUER = "backendchallenge-api";

    public String generateToken(UserDetailsImpl userDetails) {
        try {
            String primaryEmail = userDetails.getUser().getEmails().stream()
                    .findFirst()
                    .map(Email::getEmail)
                    .orElseThrow(() -> new RuntimeException("User has no email"));

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(creationDate())
                    .withExpiresAt(expirationDate())
                    .withSubject(primaryEmail)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error on generate token.", exception);
        }
    }


    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid or expirated token..");
        }
    }
    private Date creationDate() {
        Instant instant = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
        return Date.from(instant);
    }

    private Date expirationDate() {
        Instant instant = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(4).toInstant();
        return Date.from(instant);
    }
}
