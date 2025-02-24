package id.my.cupcakez.booktify.domain.user.controller;

import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.domain.rent.repository.RentQueryFilter;
import id.my.cupcakez.booktify.domain.user.repository.UserQueryFilter;
import id.my.cupcakez.booktify.domain.user.service.IUserService;
import id.my.cupcakez.booktify.dto.request.UpdateUserProfileRequest;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        UserResponse user = userService.findUserById(userId);
        ResponseWrapper<UserResponse> response = new ResponseWrapper<>(
                "User found successfully",
                user
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/rents")
    public ResponseEntity<ResponseWrapper<PagedModel<UserRentResponse>>> meRents(
            @RequestParam(value = "keyword", required = false, defaultValue = "")
            String keyword,
            @RequestParam(value = "status", required = false, defaultValue = "")
            StatusRent statusRent,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        RentQueryFilter rentQueryFilter = RentQueryFilter.builder()
                .keyword(keyword)
                .statusRent(statusRent)
                .pageable(pageable)
                .build();

        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<UserRentResponse> rents = userService.findUserRents(userId, rentQueryFilter);
        ResponseWrapper<PagedModel<UserRentResponse>> response = ResponseWrapper.<PagedModel<UserRentResponse>>builder()
                .message("User rents retrieved successfully")
                .data(new PagedModel<>(rents)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<ResponseWrapper<PagedModel<UserResponse>>> getUsers(
            @RequestParam(value = "keyword", required = false, defaultValue = "")
            String keyword,
            @RequestParam(value = "role", required = false, defaultValue = "")
            UserRole userRole,
            @ParameterObject
            @PageableDefault(sort = "created_at", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        UserQueryFilter userQueryFilter = UserQueryFilter.builder()
                .keyword(keyword)
                .role(userRole)
                .pageable(pageable)
                .build();

        Page<UserResponse> users = userService.findUsers(userQueryFilter);
        ResponseWrapper<PagedModel<UserResponse>> response = ResponseWrapper.<PagedModel<UserResponse>>builder()
                .message("Users retrieved successfully")
                .data(new PagedModel<>(users)).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
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
