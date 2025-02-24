package id.my.cupcakez.booktify.domain.auth.service;

import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.LoginResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;

public interface IAuthService {
    LoginResponse login(UserLoginRequest userLoginRequest);
    UserResponse register(UserRegisterRequest userRegisterRequest);
}
