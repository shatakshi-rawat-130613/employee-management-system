package com.project.ems.service.impl;

import com.project.ems.dto.LoginRequest;
import com.project.ems.dto.LoginResponse;
import com.project.ems.entity.Employee;
import com.project.ems.exception.InvalidCredentialsException;
import com.project.ems.repository.EmployeeRepository;
import com.project.ems.security.JwtUtil;
import com.project.ems.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl
        implements AuthenticationService {

    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {

        Employee employee = employeeRepository
                .findByEmail(request.getEmail()).orElse(null);

//                .orElseThrow(() ->
//                        new InvalidCredentialsException(
//                                "Invalid Email or Password"));

//        if (!passwordEncoder.matches(
//                request.getPassword(),
//                employee.getPassword())) {
//
//            throw new InvalidCredentialsException(
//                    "Invalid Email or Password");
//        }

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())){

            throw new InvalidCredentialsException(
                    "Invalid Email or Password");
        }


        String token =
                jwtUtil.generateToken(employee.getEmail());

        return new LoginResponse(token);
    }
}
