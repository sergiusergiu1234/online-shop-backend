package com.StefanSergiu.Licenta.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.Column;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        if (authException.getCause() instanceof ExpiredJwtException) {
            response.getOutputStream().println("{ \"error\": \"JWT token has expired\" }");
        } else {
            response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
        }
    }
}
