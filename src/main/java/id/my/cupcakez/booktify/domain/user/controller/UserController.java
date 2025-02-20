package id.my.cupcakez.booktify.domain.user.controller;

import id.my.cupcakez.booktify.domain.user.service.IUserService;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "users", description = "Users API")
public class UserController {
    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Map<String, Object>> updateUser(
            @Validated
            @RequestBody
            UpdateUserRequest updateUserRequest
    ) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserResponse user = userService.updateUser(userId, updateUserRequest);

        Map<String, Object> response = Map.of(
                "message", "User updated successfully",
                "user" , user
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(
    ) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        System.out.println("userId: " + userId);
        UserResponse user = userService.getUserById(userId);

        Map<String, Object> response = Map.of(
                "message", "User retrified successfully",
                "user" , user
        );

        return ResponseEntity.ok(response);
    }
}
