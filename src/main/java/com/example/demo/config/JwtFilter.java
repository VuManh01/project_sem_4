package com.example.demo.config;

import com.example.demo.services.JWTService;
import com.example.demo.services.MyUserDetailsService;
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
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        // Log cho 2 endpoint cụ thể
//        String requestURI = request.getRequestURI();
//        if (requestURI.equals("/api/public/songs/mostListened") || requestURI.equals("/api/public/users")) {
//            System.out.println("\n=== Debug Token for " + requestURI + " ===");
//            System.out.println("Authorization Header: " + authHeader);
//        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
            
//            if (requestURI.equals("/api/public/songs/mostListened") || requestURI.equals("/api/public/users")) {
//                System.out.println("Token: " + token);
//                System.out.println("Username from token: " + username);
//            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
//                if (requestURI.equals("/api/public/songs/mostListened") || requestURI.equals("/api/public/users")) {
//                    System.out.println("Authentication successful for: " + username);
//                    System.out.println("User authorities: " + userDetails.getAuthorities());
//                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
