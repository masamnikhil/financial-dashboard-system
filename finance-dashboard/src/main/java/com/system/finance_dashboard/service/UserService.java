package com.system.finance_dashboard.service;

import com.system.finance_dashboard.dto.LoginRequest;
import com.system.finance_dashboard.dto.RegisterRequest;

import java.util.Map;


public interface UserService {

    Map<String, String> registerUser(RegisterRequest request);
    Map<String, String> authenticate(LoginRequest request);
}
