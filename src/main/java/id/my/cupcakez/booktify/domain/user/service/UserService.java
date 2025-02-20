package id.my.cupcakez.booktify.domain.user.service;

import id.my.cupcakez.booktify.domain.rent.repository.IRentRepository;
import id.my.cupcakez.booktify.domain.user.repository.IUserRepository;
import id.my.cupcakez.booktify.dto.request.UpdateUserRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.entity.UserEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.mapper.IRentMapper;
import id.my.cupcakez.booktify.util.mapper.IUserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IRentMapper rentMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    public UserService(IUserRepository userRepository, IRentRepository rentRepository,IUserMapper userMapper, IRentMapper rentMapper,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
        this.userMapper = userMapper;
        this.rentMapper = rentMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserResponse updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return userRepository.findById(userId).map(u -> {
            Optional.ofNullable(updateUserRequest.getName()).ifPresent(u::setName);
            Optional.ofNullable(updateUserRequest.getEmail()).ifPresent(u::setEmail);
            Optional.ofNullable(updateUserRequest.getPhone()).ifPresent(u::setPhone);
            Optional.ofNullable(updateUserRequest.getAddress()).ifPresent(u::setAddress);

            if (
                    !(updateUserRequest.getPassword() == null) &&
                    !(updateUserRequest.getConfirmPassword() == null) &&
                    (updateUserRequest.getPassword().equals(updateUserRequest.getConfirmPassword()))
            ) {
                u.setPassword(bCryptPasswordEncoder.encode(updateUserRequest.getPassword()));
            }

            UserEntity userSaved = userRepository.save(u);

            return userMapper.toUserResponse(userSaved);
        }).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userEntity -> {
                    List<RentEntity> rentEntities = rentRepository.getRentByUserId(userId);

                    if (rentEntities.isEmpty()) {
                        return userMapper.toUserResponse(userEntity);
                    }

                    List<RentResponse> rentResponseList = rentEntities.stream()
                            .map(rentMapper::toRentResponseWithoutUser)
                            .toList();

                    UserResponse userResponse = userMapper.toUserResponse(userEntity);
                    userResponse.setRents(rentResponseList);

                    return userResponse;
                })
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND)
                );
    }

}
