package com.project.ems.service;

import com.project.ems.dto.LoginRequest;
import com.project.ems.dto.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

}
