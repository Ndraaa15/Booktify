package id.my.cupcakez.booktify.domain.user.service;

import id.my.cupcakez.booktify.domain.rent.repository.IRentRepository;
import id.my.cupcakez.booktify.domain.rent.repository.RentQueryFilter;
import id.my.cupcakez.booktify.domain.user.repository.IUserRepository;
import id.my.cupcakez.booktify.domain.user.repository.UserQueryFilter;
import id.my.cupcakez.booktify.dto.request.UpdateUserProfileRequest;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.UserEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.mapper.IUserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private IUserRepository userRepository;
    private IRentRepository rentRepository;
    private IUserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    public UserService(IUserRepository userRepository, IRentRepository rentRepository,IUserMapper userMapper,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @CacheEvict(value = {"users", "user"}, allEntries = true)
    public UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return userRepository.findById(userId).map(u -> {
            Optional.ofNullable(updateUserRequest.getName()).ifPresent(u::setName);
            Optional.ofNullable(updateUserRequest.getEmail()).ifPresent(u::setEmail);
            Optional.ofNullable(updateUserRequest.getPhone()).ifPresent(u::setPhone);
            Optional.ofNullable(updateUserRequest.getAddress()).ifPresent(u::setAddress);
            Optional.ofNullable(updateUserRequest.getRole()).ifPresent(u::setRole);

            if (
                    !(updateUserRequest.getPassword() == null) &&
                    !(updateUserRequest.getConfirmPassword() == null) &&
                    (updateUserRequest.getPassword().equals(updateUserRequest.getConfirmPassword()))
            ) {
                u.setPassword(bCryptPasswordEncoder.encode(updateUserRequest.getPassword()));
            }

            UserEntity userSaved = userRepository.save(u);

            logger.info("User with id {} successfully updated", userSaved.getId());

            return userMapper.toUserResponse(userSaved);
        }).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    @Cacheable(value = "user", key = "'user-' + #userId")
    public UserResponse findUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userEntity -> {
                    logger.info("User with id {} successfully found", userEntity.getId());
                    return userMapper.toUserResponse(userEntity);
                })
                .orElseThrow(() -> new CustomException(String.format("User with id %s not found", userId), HttpStatus.NOT_FOUND)
                );
    }

    @Override
    @Cacheable(value = "user-rents", key = "'users-' + #userId + '-' + #pageable")
    public Page<UserRentResponse> findUserRents(UUID userId, RentQueryFilter rentQueryFilter) {
        Page<UserRentResponse> userRents;

        if (rentQueryFilter.getStatusRent() == null){
            userRents = userRepository.findRentsByUserId(userId, rentQueryFilter.getKeyword(), rentQueryFilter.getPageable())
                    .map(rentEntity -> {
                        return userMapper.toUserRentResponse(rentEntity);
                    });
        }else {
            userRents = userRepository.findRentsByUserId(userId, rentQueryFilter.getKeyword(), rentQueryFilter.getStatusRent(), rentQueryFilter.getPageable())
                    .map(rentEntity -> {
                        return userMapper.toUserRentResponse(rentEntity);
                    });
        }

        logger.info("User rents for pages {} , size {}, sort {}, keyword {}, status {} successfully found",
                rentQueryFilter.getPageable().getPageNumber(),
                rentQueryFilter.getPageable().getPageSize(),
                rentQueryFilter.getPageable().getSort(),
                rentQueryFilter.getKeyword(),
                rentQueryFilter.getStatusRent()
        );

        return userRents;
    }

    @Override
    @Cacheable(value = "users", key = "'users-' + #pageable")
    public Page<UserResponse> findUsers(UserQueryFilter userQueryFilter) {
        Page<UserResponse> users;

        if (userQueryFilter.getRole() == null){
            users = userRepository.findAll(userQueryFilter.getKeyword(), userQueryFilter.getPageable())
                    .map(userEntity -> {
                        return userMapper.toUserResponse(userEntity);
                    });

        }else {
            users = userRepository.findAll(userQueryFilter.getKeyword(), userQueryFilter.getRole(), userQueryFilter.getPageable())
                    .map(userEntity -> {
                        return userMapper.toUserResponse(userEntity);
                    });
        }

        logger.info("Users for pages {} , size {}, sort {}, keyword {}, role {} successfully found",
                userQueryFilter.getPageable().getPageNumber(),
                userQueryFilter.getPageable().getPageSize(),
                userQueryFilter.getPageable().getSort(),
                userQueryFilter.getKeyword(),
                userQueryFilter.getRole()
        );

        return users;
    }

    @Override
    @CacheEvict(value = {"user", "users"}, allEntries = true)
    public UserResponse updateUserProfile(UUID userId, UpdateUserProfileRequest updateUserProfileRequest) {
        return userRepository.findById(userId).map(u -> {
            Optional.ofNullable(updateUserProfileRequest.getName()).ifPresent(u::setName);
            Optional.ofNullable(updateUserProfileRequest.getEmail()).ifPresent(u::setEmail);
            Optional.ofNullable(updateUserProfileRequest.getPhone()).ifPresent(u::setPhone);
            Optional.ofNullable(updateUserProfileRequest.getAddress()).ifPresent(u::setAddress);

            if (
                    !(updateUserProfileRequest.getPassword() == null) &&
                            !(updateUserProfileRequest.getConfirmPassword() == null) &&
                            (updateUserProfileRequest.getPassword().equals(updateUserProfileRequest.getConfirmPassword()))
            ) {
                u.setPassword(bCryptPasswordEncoder.encode(updateUserProfileRequest.getPassword()));
            }

            UserEntity userSaved = userRepository.save(u);

            logger.info("User profile with id {} successfully updated", userSaved.getId());

            return userMapper.toUserResponse(userSaved);
        }).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND)
        );
    }
}
