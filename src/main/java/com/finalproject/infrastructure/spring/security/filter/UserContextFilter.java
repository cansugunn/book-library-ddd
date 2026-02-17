package com.finalproject.infrastructure.spring.security.filter;

import com.finalproject.domain.valueobject.UserType;
import com.finalproject.infrastructure.common.security.UserContext;
import com.finalproject.infrastructure.common.security.UserContextHolder;
import com.finalproject.infrastructure.spring.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class UserContextFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    public UserContextFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header");
                return;
            }

            String token = authHeader.substring(BEARER_PREFIX.length());
            Claims claims = jwtTokenProvider.parseToken(token);

            Integer userId = claims.get("userId", Integer.class);
            String username = claims.getSubject();
            String userType = claims.get("userType", String.class);

            if (userId == null || username == null || userType == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token claims");
                return;
            }

            UserContextHolder.set(new UserContext(userId, username, UserType.valueOf(userType)));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + userType))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException ex) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired token");
        } finally {
            SecurityContextHolder.clearContext();
            UserContextHolder.clear();
        }
    }
}
