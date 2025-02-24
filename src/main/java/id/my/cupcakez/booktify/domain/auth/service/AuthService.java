package id.my.cupcakez.booktify.domain.auth.service;


import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.domain.auth.repository.IAuthRepository;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.LoginResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.UserEntity;

import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.jwt.IJwt;
import id.my.cupcakez.booktify.util.mapper.IUserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService implements IAuthService {
    private IAuthRepository authRepository;
    private IUserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private IJwt jwt;
    private static final Logger logger = LogManager.getLogger(AuthService.class);


    @Autowired
    public AuthService(IAuthRepository authRepository, IUserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder, IJwt jwt) {
        this.authRepository = authRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwt = jwt;
    }

    @Override
    public LoginResponse login(
            UserLoginRequest userLoginRequest
    ) {
        Optional<UserEntity> userEntity = authRepository.findByEmail(userLoginRequest.getEmail());
        return userEntity.map(u -> {
            if (bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), u.getPassword())) {
                logger.info("user with email {} successfuly login", u.getEmail());

                LoginResponse loginResponse = LoginResponse.builder()
                        .email(u.getEmail())
                        .role(u.getRole())
                        .token(jwt.generateToken(u))
                        .build();
                return loginResponse;
            } else {
                throw new CustomException("invalid email or password", HttpStatus.UNAUTHORIZED);
            }
        }).orElseThrow(
                () -> new CustomException("user not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponse register(
            UserRegisterRequest userRegisterRequest
    ) {
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            throw new CustomException("password and confirm password must be the same", HttpStatus.BAD_REQUEST);
        }

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!Pattern.matches(passwordPattern, userRegisterRequest.getPassword())) {
            throw new CustomException("password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character", HttpStatus.BAD_REQUEST);
        }

        UserEntity userData = UserEntity
                .builder()
                .name(userRegisterRequest.getName())
                .email(userRegisterRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(userRegisterRequest.getPassword()))
                .role(UserRole.USER)
                .address(userRegisterRequest.getAddress())
                .phone(userRegisterRequest.getPhone())
                .build();

        UserEntity user = authRepository.save(userData);

        logger.debug("user with email {} successfuly created", user.getEmail());

        return userMapper.toUserResponse(user);
    }
}
