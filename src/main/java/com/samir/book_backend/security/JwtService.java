package com.samir.book_backend.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String SECRET_KEY = "61c8ab167695d110bb031120a73be1824e46dac287b12a1d8b3bc7083fd3852846ee38c7bf670840429f6707bcdd7ffb7e9c52e694f1199dd7d4fcf03d914097552104da0aeedbc227c187d90249b15f23a85b7dc5df512dada2b55e44b05db34973e4b0ade8dc8bcd07662f0b1e6db137e3af83b57deeb74e477ae1589962ec71b6a3ac5c228d374fa71aefe2654fb6f9e0c665b608a460ff8fcfb468b38bac04b4dd3676cc3cb418651722b7b45de12fff1719ffbc91da8fe915d0ab0bf5cc7f00560061d91115b3b3bbfe35cb172cf7cce040d32660c9e2a4b1a63db5782d531bee57fd892d5364a0d72c462f4e4fd1750a31aac1b9968f4f1645ef54e66e";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)   //Signing key is the secret that is used to create signature part of jwt which is used to verify that the sender of jwt is who it claims to be and ensures messages was not changed along.
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(HashMap<String, Object> claims, UserDetails userDetails) {
        claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}