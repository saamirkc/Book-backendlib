package com.samir.book_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor   //automatically create constructor for final
//after creating user and extending user details in user. the first thing that runs is JWT Filter can view screenshot of security .
//The first thing that the jwt filter do is checking if jwt token is there or not.
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");   //here authHeader contains The Authorization header if present.
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);                    //move to next filter
            return;
        }
        jwt = authHeader.substring(7);  //else if Authorization is there then as we know string authHeader contains Authorization from 37.
                                                  //and Authorization(authHeader) starts with Bearer token .so jwt contains all string after bearer and space which is 7 letters.


          //now after checking jwt token what we need to do is to call UserDetailsService to check if we have user already in database or not.
//        to do that we need to call JwtService to extract username/userEmail.i.e at first after checking jwt token the jwt filter is responsible for extracting
//        username or userEmail
//        userEmail=//todo extract the userEmail from JWT token
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            now after extracting userEmail by making Jwt service( VALIDATING JWT) .next step is to get user from user details sevice , user details service contains loadUserbyUsername.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);  //can also write  User user = this.userDetailsService.loadUserByUsername(userEmail); as user also implements userdetails
            // .now if jwt token is valid our goal is to update security context and send our request to dispatcher servlet(look in screenshot for visualizing).
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken); //updating security context
            }
        }
        filterChain.doFilter(request, response);
    }

//    key up to here at first create jwt filter which check jwt token and also extract username. In order to extract username we create
//    Jwt Service which validates jwt . it also generate token along with extracting username.After extracting username , we call user from
//    user details service through loaduserbyusername (inbuilt function).After this, we update security context.
//    now to make all these work properly we need to do extra step . Th extra step is to tell spring  which configuration to use in order
//    to make all these work.We perform all things but what we are missing is binding .
//    For that we create SecurityConfig.
}