package com.lapaix.SBauth3.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * By Gershom
 * This class will run for each request sent and checks if a Bearer token is present in the Authorization Header
 *
 * if the token is present, it will be verified and the authentication data will be set fot the user for that request by
 *    "SecurityContextHolder"
 * */
@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired

    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            String jwt = authHeader.substring(7);
            if(jwt == null || jwt.isBlank()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer Header");
            }else{
                try{
                    String email = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());

                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }catch (JWTVerificationException ex){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
