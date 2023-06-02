package com.example.bugsystem.servicies;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    private final static String JWT_KEY = "6B597033733676397924423F4528482B4D6251655468576D5A7134743777217A";
    private final static Integer EXPIRATION_TIME_MILLS = 3600 * 1000;

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims allClaims = extractAllClaims(jwt);
        return claimsResolver.apply(allClaims);
    }

    public String generateJwt(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + EXPIRATION_TIME_MILLS)))
                .signWith(getSigningSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isJwtValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isJwtExpired(jwt);


    }

    private boolean isJwtExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private SecretKey getSigningSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
