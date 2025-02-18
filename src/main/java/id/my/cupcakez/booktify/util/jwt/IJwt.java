package id.my.cupcakez.booktify.util.jwt;

import id.my.cupcakez.booktify.entity.UserEntity;
import id.my.cupcakez.booktify.security.auth.JwtAuth;
import org.springframework.security.core.Authentication;

public interface IJwt {
    String generateToken(UserEntity userEntity);
    boolean validateJwtToken(String token);
    JwtAuth getAuthenticationFromToken(String token);
}
