package id.my.cupcakez.booktify.domain.auth.controller;

import id.my.cupcakez.booktify.domain.auth.service.AuthService;
import id.my.cupcakez.booktify.domain.auth.service.IAuthService;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "auth", description = "Auth API")
public class AuthController {
    private IAuthService authService;

    @Autowired
    public AuthController(AuthService authServiceImpl) {
        this.authService = authServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated
            @RequestBody
            UserLoginRequest userLoginRequest
    ) {
        String token = authService.login(userLoginRequest);

        Map<String, Object> response = Map.of(
                "message", "Login success",
                "token", token
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Validated
            @RequestBody
            UserRegisterRequest userRegisterRequest
    ) {
       UserResponse user = authService.register(userRegisterRequest);

       Map<String, Object> response = Map.of(
                "message", "Register success",
                "user", user
       );

       return ResponseEntity.created(URI.create("/api/v1/auth/register")).body(response);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF')")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'STAFF')")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok().build();
    }
}
