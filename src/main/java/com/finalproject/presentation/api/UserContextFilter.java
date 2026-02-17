package com.finalproject.presentation.api;

import com.finalproject.domain.valueobject.UserType;
import com.finalproject.infrastructure.security.UserContext;
import com.finalproject.infrastructure.security.UserContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserContextFilter extends OncePerRequestFilter {
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USERNAME_HEADER = "X-Username";
    private static final String USER_TYPE_HEADER = "X-User-Type";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String userId = request.getHeader(USER_ID_HEADER);
            String username = request.getHeader(USERNAME_HEADER);
            String userType = request.getHeader(USER_TYPE_HEADER);

            if (userId != null && username != null && userType != null) {
                UserContextHolder.set(new UserContext(Integer.parseInt(userId), username, UserType.valueOf(userType)));
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
        }
    }
}
