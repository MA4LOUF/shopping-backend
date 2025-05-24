package com.chris.shopping.service.Interface;

import com.chris.shopping.dto.AuthenticationDTO;
import com.chris.shopping.model.UserLoginRequest;
import com.chris.shopping.model.UserRegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AuthenticationDTO registerUser(UserRegisterRequest request);
    AuthenticationDTO loginUser(UserLoginRequest request);
}
