package org.example.blogapp.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServices {

    private final String secretKey="D37C74B31448766BAC4BEAC32AC88F106B5BC45517B1CBEA5C99EB43B0384DB2";

    // TimeUnit.MINUTES.toMinutes(30);
    private final long VALIDITY= 5*60*60;


    public String getUsernameFromToken(String token)
    {
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }

    private SecretKey getKey() {

        byte[] key= Base64.getDecoder().decode(secretKey);

        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(UserDetails userDetails)
    {
        Map<String,Object> claims=new HashMap<>();

        claims.put("User",userDetails.getUsername());
        claims.put("Roles",userDetails.getAuthorities());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+VALIDITY*1000))
                .signWith(getKey())
                .compact();
    }

    boolean isTokenExpired(String token)
    {

        Date date=getClaimsFromToken(token).getExpiration();

        return date.before(new Date());
    }

    public boolean validateToken(String token,UserDetails userDetails)
    {
        String username=getUsernameFromToken(token);

        return username.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }
}
