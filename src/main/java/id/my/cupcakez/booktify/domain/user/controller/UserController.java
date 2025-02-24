package id.my.cupcakez.booktify.domain.user.controller;

import id.my.cupcakez.booktify.domain.user.service.IUserService;
import id.my.cupcakez.booktify.dto.request.UpdateUserProfileRequest;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
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
    public ResponseEntity<ResponseWrapper<UserResponse>> updateUser(
            @Validated
            @RequestBody
            UpdateUserProfileRequest updateUserProfileRequest
    ) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserResponse user = userService.updateUserProfile(userId, updateUserProfileRequest);
        ResponseWrapper<UserResponse> response = new ResponseWrapper<>(
                "User updated successfully",
                user
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapper<UserResponse>> me(
    ) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserResponse user = userService.getUserById(userId);
        ResponseWrapper<UserResponse> response = new ResponseWrapper<>(
                "User found successfully",
                user
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/rents")
    public ResponseEntity<ResponseWrapper<PagedModel<UserRentResponse>>> meRents(
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<UserRentResponse> rents = userService.getUserRents(userId, pageable);
        ResponseWrapper<PagedModel<UserRentResponse>> response = ResponseWrapper.<PagedModel<UserRentResponse>>builder()
                .message("user rents retrieved successfully")
                .data(new PagedModel<>(rents)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ResponseWrapper<PagedModel<UserResponse>>> getUsers(
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<UserResponse> users = userService.getUsers(pageable);
        ResponseWrapper<PagedModel<UserResponse>> response = ResponseWrapper.<PagedModel<UserResponse>>builder()
                .message("users retrieved successfully")
                .data(new PagedModel<>(users)).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ResponseWrapper<UserResponse>> updateUser(
            @PathVariable("id") UUID userId,
            @Validated
            @RequestBody
            UpdateUserRequest updateUserRequest
    ) {
        UserResponse user = userService.updateUser(userId, updateUserRequest);
        ResponseWrapper<UserResponse> response = new ResponseWrapper<>(
                "User updated successfully",
                user
        );
        return ResponseEntity.ok(response);
    }
}
