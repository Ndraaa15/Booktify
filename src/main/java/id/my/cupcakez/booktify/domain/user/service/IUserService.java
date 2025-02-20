package id.my.cupcakez.booktify.domain.user.service;

import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.UserResponse;

import java.util.UUID;

public interface IUserService {
    UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest);

    UserResponse getUserById(UUID userId);
}
