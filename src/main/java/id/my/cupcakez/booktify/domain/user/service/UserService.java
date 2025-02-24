package id.my.cupcakez.booktify.domain.user.service;

import id.my.cupcakez.booktify.domain.rent.repository.IRentRepository;
import id.my.cupcakez.booktify.domain.user.repository.IUserRepository;
import id.my.cupcakez.booktify.dto.request.UpdateUserProfileRequest;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.entity.UserEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.mapper.IRentMapper;
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

            logger.info("user with id {} successfully updated", userSaved.getId());

            return userMapper.toUserResponse(userSaved);
        }).orElseThrow(
                () -> new CustomException("user not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    @Cacheable(value = "user", key = "'user-' + #userId")
    public UserResponse getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userEntity -> {
                    logger.info("user with id {} successfully found", userEntity.getId());
                    return userMapper.toUserResponse(userEntity);
                })
                .orElseThrow(() -> new CustomException(String.format("user with id %s not found", userId), HttpStatus.NOT_FOUND)
                );
    }

    @Override
    @Cacheable(value = "user-rents", key = "'users-' + #userId + '-' + #pageable")
    public Page<UserRentResponse> getUserRents(UUID userId, Pageable pageable) {
        return userRepository.findRentsByUserId(userId, pageable)
                .map(rentEntity -> {
                    logger.info("rents for user with id {} successfully found", userId);
                    return userMapper.toUserRentResponse(rentEntity);
                });
    }

    @Override
    @Cacheable(value = "users", key = "'users-' + #pageable")
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userEntity -> {
                    logger.info("users for page {} successfully found", pageable.getPageNumber());
                    return userMapper.toUserResponse(userEntity);
                });
    }

    @Override
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

            logger.info("user profile with id {} successfully updated", userSaved.getId());

            return userMapper.toUserResponse(userSaved);
        }).orElseThrow(
                () -> new CustomException("user not found", HttpStatus.NOT_FOUND)
        );
    }

}
