package com.chris.shopping.service.impl;

import com.chris.shopping.dto.AuthenticationDTO;
import com.chris.shopping.exception.AccountNotFoundException;
import com.chris.shopping.exception.IncorrectPasswordException;
import com.chris.shopping.exception.UserAlreadyExistsException;
import com.chris.shopping.model.*;
import com.chris.shopping.repository.UserRepository;
import com.chris.shopping.service.Interface.UserService;
import com.chris.shopping.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            return new UserPrincipal(user.get());
        }
        throw new UsernameNotFoundException("User with email " + email + "does not exist");
    }

    @Override
    @Transactional
    public AuthenticationDTO registerUser(UserRegisterRequest request) {
        if(this.userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("this email is already associated with another account, please use another email");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return new AuthenticationDTO(jwtUtil.generateToken(new UserPrincipal(user)), user.getFirstName(), user.getLastName());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationDTO loginUser(UserLoginRequest request) {
        Optional<User> user = this.userRepository.findByEmail(request.email());
        if(user.isEmpty()) {
            throw new AccountNotFoundException("this email is not linked to any account, if you would like to create an account, please register");
        }
        if(!encoder.matches(request.password(), user.get().getPassword())) {
            throw new IncorrectPasswordException("Incorrect password, please try again");
        }

        return new AuthenticationDTO(jwtUtil.generateToken(new UserPrincipal(user.get())), user.get().getFirstName(), user.get().getLastName());
    }
}
