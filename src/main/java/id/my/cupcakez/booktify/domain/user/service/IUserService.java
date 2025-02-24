package id.my.cupcakez.booktify.domain.user.service;

import id.my.cupcakez.booktify.dto.request.UpdateUserProfileRequest;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {
    UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest);

    UserResponse getUserById(UUID userId);

    Page<UserRentResponse> getUserRents(UUID userId, Pageable pageable);

    Page<UserResponse> getUsers(Pageable pageable);

    UserResponse updateUserProfile(UUID userId, UpdateUserProfileRequest updateUserProfileRequest);
}
