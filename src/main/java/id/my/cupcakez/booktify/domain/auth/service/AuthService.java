package id.my.cupcakez.booktify.domain.auth.service;


import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.domain.auth.repository.IAuthRepository;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.UserEntity;

import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.jwt.IJwt;
import id.my.cupcakez.booktify.util.mapper.IUserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String login(UserLoginRequest userLoginRequest) {
        Optional<UserEntity> userEntity = authRepository.findByEmail(userLoginRequest.getEmail());

        return userEntity.map(u -> {
            if (bCryptPasswordEncoder.matches(userLoginRequest.getPassword(), u.getPassword())) {
                logger.debug("User logged in: {}", u);
                return jwt.generateToken(u);
            } else {
                throw new CustomException("Invalid email or password", HttpStatus.UNAUTHORIZED);
            }
        }).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND)
        );
    }

    public UserResponse register(UserRegisterRequest userRegisterRequest) {
        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getConfirmPassword())) {
            throw new CustomException("Password and confirm password must be the same", HttpStatus.BAD_REQUEST);
        }

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!Pattern.matches(passwordPattern, userRegisterRequest.getPassword())) {
            throw new CustomException("Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character", HttpStatus.BAD_REQUEST);
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

        logger.debug("User created: {}", user);

        return userMapper.toUserResponse(user);
    }
}
