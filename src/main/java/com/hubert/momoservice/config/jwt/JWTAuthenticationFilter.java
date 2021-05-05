package com.hubert.momoservice.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.entity.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.hubert.momoservice.config.jwt.SecurityConstants.EXPIRATION_TIME;
import static com.hubert.momoservice.config.jwt.SecurityConstants.SECRET;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;

    setFilterProcessesUrl("/api/v1/users/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
      HttpServletResponse res) throws AuthenticationException {
    try {
      AppUser appUser = new ObjectMapper()
          .readValue(req.getInputStream(), AppUser.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              appUser.getUsername(),
              appUser.getPassword(),
              appUser.getAuthorities())
      );
    } catch (IOException e) {
      try {
        res.sendError(HttpServletResponse.SC_FORBIDDEN, "Failed to authenticate user!");
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }

      throw new RuntimeException("Failed to authenticate user!");
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain,
      Authentication auth) throws IOException {

    if (!((AppUser) auth.getPrincipal()).getEnabled()) {
      res.getWriter().write("{\"error\": \"User not enabled!\"}");
      res.setContentType("JSON");
      res.sendError(HttpServletResponse.SC_FORBIDDEN);
      res.getWriter().flush();

      throw new BadRequestException("User not enabled");
    }
    String token = JWT.create()
        .withSubject(((AppUser) auth.getPrincipal()).getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SECRET.getBytes()));

    res.addHeader("Authorization", "Bearer " + token);

    res.getWriter().write(String.format("{\"token\": \"%s\"}", token));
    res.setContentType("JSON");
    res.getWriter().flush();
  }
}