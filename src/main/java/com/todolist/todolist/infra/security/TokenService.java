package com.todolist.todolist.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.todolist.todolist.domain.user.User;

@Service
public class TokenService {

  private String jwtSecret = "mysecret";

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
      String token = JWT.create()
          .withIssuer("auth-api")
          .withSubject(user.getLogin())
          .withExpiresAt(getExpirationDate())
          .sign(algorithm);
      return token;
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error generating token", exception);
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
      JWT.require(algorithm).build().verify(token);
      return JWT.decode(token).getSubject();

    } catch (JWTVerificationException exception) {
      throw new RuntimeException("Error validating token", exception);
    }
  }

  private Instant getExpirationDate() {
    return LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
