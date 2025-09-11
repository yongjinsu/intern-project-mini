package org.example.tttn.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.tttn.service.interfaces.IRedisService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final IRedisService redisService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (
                path.startsWith("/api/v1/auth") ||
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/v3/api-docs") ||
                        path.startsWith("/swagger-resources") ||
                        path.startsWith("/webjars") ||
                        path.equals("/swagger-ui.html") ||
                        path.equals("/error")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token == null) {
            token = request.getHeader("accessToken");
        }

        logger.debug("Processing request to: {} with token present: {}", path, token != null);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String jti = jwtUtil.getJtiFromToken(token);
                    if (jti == null || !redisService.isTokenBlacklisted(jti)) {
                        Long userId = jwtUtil.getUserIdFromToken(token);
                        Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();

                        UserDetails userDetails = User.withUsername(userId.toString())
                                .password("")
                                .authorities(authorities)
                                .build();

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("Successfully authenticated user: {}", userId);
                    } else {
                        logger.debug("Token is blacklisted");
                    }
                } else {
                    logger.debug("Token validation failed");
                }
            } catch (Exception e) {
                logger.debug("Error processing JWT token: {}", e.getMessage());
            }
        } else {
            logger.debug("No valid Bearer token found in request");
        }
        filterChain.doFilter(request, response);
    }
}
