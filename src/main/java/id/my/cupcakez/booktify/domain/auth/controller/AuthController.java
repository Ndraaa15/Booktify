package id.my.cupcakez.booktify.domain.auth.controller;

import id.my.cupcakez.booktify.domain.auth.service.AuthService;
import id.my.cupcakez.booktify.domain.auth.service.IAuthService;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.LoginResponse;
import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
    public ResponseEntity<ResponseWrapper<LoginResponse>> login(
            @Validated
            @RequestBody
            UserLoginRequest userLoginRequest
    ) {
        LoginResponse loginResponse = authService.login(userLoginRequest);
        ResponseWrapper<LoginResponse> response = ResponseWrapper.<LoginResponse>builder()
                .message("login success")
                .data(loginResponse)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<UserResponse>> register(
            @Validated
            @RequestBody
            UserRegisterRequest userRegisterRequest
    ) {
       UserResponse user = authService.register(userRegisterRequest);
       ResponseWrapper<UserResponse> response = ResponseWrapper.<UserResponse>builder()
                .message("register success")
                .data(user)
                .build();
       return ResponseEntity.created(URI.create("/api/v1/auth/register")).body(response);
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseWrapper<String>> logout(HttpSession session) {
        // Todo : delete sessions in table

        session.invalidate();
        ResponseWrapper<String> response = ResponseWrapper.<String>builder()
                .message("logout success")
                .data("logout")
                .build();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok().build();
    }
}
