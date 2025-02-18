package id.my.cupcakez.booktify.util.jwt;

import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.entity.UserEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.security.auth.JwtAuth;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
@Component
public class Jwt implements IJwt {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(UserEntity userEntity) {
        Map<String, Object> claims = Map.of(
                "role", userEntity.getRole()
        );

        return Jwts.builder()
                .claims(claims)
                .issuer("Booktify")
                .subject(userEntity.getEmail())
                .id(userEntity.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public JwtAuth getAuthenticationFromToken(String token) {
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String roleString = claims.get("role", String.class);
        UserRole role = UserRole.valueOf(roleString);

        List<SimpleGrantedAuthority> roles;
        if (roleString == null) {
            roles = List.of();
        } else {
            roles = List.of(new SimpleGrantedAuthority(role.name()));
        }

        return new JwtAuth(claims.getId(), null, roles);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            throw new CustomException("Invalid JWT signature: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            throw new CustomException("Invalid JWT token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new CustomException("JWT token is expired: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException("JWT token is unsupported: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            throw new CustomException("JWT claims string is empty: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}

