package com.StefanSergiu.Licenta.filter;


import com.StefanSergiu.Licenta.service.JwtService;
import com.StefanSergiu.Licenta.service.UserInfoUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try {
          String authHeader = request.getHeader("Authorization");
          String token = null;
          String username = null;
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
              token = authHeader.substring(7);
              username = jwtService.extractUsername(token);
          }

          if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
              UserDetails userDetails = userDetailsService.loadUserByUsername(username);

              if (jwtService.validateToken(token, userDetails)) {
                  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authToken);
              }
          }
          filterChain.doFilter(request, response);
      } catch (ExpiredJwtException e){
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{ \"error\": \"JWT token has expired\" }");
      }
    }
}