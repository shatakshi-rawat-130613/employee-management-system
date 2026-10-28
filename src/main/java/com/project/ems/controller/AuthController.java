package com.project.ems.controller;

import com.project.ems.dto.LoginRequest;
import com.project.ems.dto.LoginResponse;
import com.project.ems.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        return authenticationService.login(request);
    }
}
