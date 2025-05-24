package com.chris.shopping.controller;

import com.chris.shopping.dto.AuthenticationDTO;
import com.chris.shopping.dto.ErrorResponseDTO;
import com.chris.shopping.model.UserLoginRequest;
import com.chris.shopping.model.UserRegisterRequest;
import com.chris.shopping.service.Interface.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description ="creates a new user with the corresponding information and returns a JWT token", tags = {"Authentication"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = AuthenticationDTO.class))),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public AuthenticationDTO register(@RequestBody @Valid UserRegisterRequest request) {
        return this.userService.registerUser(request);
    }
    @PostMapping("/login")
    @Operation(summary = "Logs a user into the website", tags = {"Authentication"},  description ="takes the user's account and password, if they are valid then it returns a JWT token, if not it returns an error")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = AuthenticationDTO.class))),
            @ApiResponse(responseCode = "401", description = "incorrect username or password", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public AuthenticationDTO login(@RequestBody @Valid UserLoginRequest request) {
        return this.userService.loginUser(request);
    }
}
