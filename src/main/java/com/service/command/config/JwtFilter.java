package com.service.command.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final ConfigAcces configAcces;

    public JwtFilter(ConfigAcces configAcces) {
        this.configAcces = configAcces;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        // 1. Buscar la cookie llamada "jwt_gozu"
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt_gozu")) {
                    token = cookie.getValue();
                }
            }
        }

        // 2. Si hay token y es válido, configuramos a Spring Security
        if (token != null && configAcces.validateToken(token)) {

            Claims claims = configAcces.getAllClaimsFromToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class); // Extraemos el "ROLE_Admin"

            // 3. Le decimos a Spring Security quién es y qué rol tiene
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singletonList(authority));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 4. Deja que la petición siga su camino hacia tu controlador
        filterChain.doFilter(request, response);
    }
}