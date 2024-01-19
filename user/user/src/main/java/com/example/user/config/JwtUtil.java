package com.example.user.config;

import com.example.user.dtos.UserDTO;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final String secretKey = "mysecretkey";
    private final long accessTokenValidity = 60*60*1000;

    private final JwtParser jwtParser;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";


    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createToken(UserDTO userDTO) {
        Claims claims = Jwts.claims().setSubject(userDTO.getUsername());
        claims.put("name", userDTO.getName());
        claims.put("id", userDTO.getId());
        claims.put("role", userDTO.getRole().toString());

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest request) {
        String token = null;
        try {
            token = resolveToken(request);
        } catch (ExpiredJwtException e) {
            request.setAttribute("expired", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("invalid", e.getMessage());
            throw e;
        }

        return (token != null) ? parseJwtClaims(token) : null;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }
}
