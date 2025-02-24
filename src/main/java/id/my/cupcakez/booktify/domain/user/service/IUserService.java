package id.my.cupcakez.booktify.domain.user.service;

import id.my.cupcakez.booktify.domain.rent.repository.RentQueryFilter;
import id.my.cupcakez.booktify.domain.user.repository.UserQueryFilter;
import id.my.cupcakez.booktify.dto.request.UpdateUserProfileRequest;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {
    UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest);

    UserResponse findUserById(UUID userId);

    Page<UserRentResponse> findUserRents(UUID userId, RentQueryFilter rentQueryFilter);

    Page<UserResponse> findUsers(UserQueryFilter userQueryFilter);

    UserResponse updateUserProfile(UUID userId, UpdateUserProfileRequest updateUserProfileRequest);
}
