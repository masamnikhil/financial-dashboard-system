package com.system.finance_dashboard.service;

import com.system.finance_dashboard.dto.LoginRequest;
import com.system.finance_dashboard.dto.RegisterRequest;
import com.system.finance_dashboard.dto.UserDto;
import com.system.finance_dashboard.entity.Role;
import com.system.finance_dashboard.entity.Status;

import java.util.List;
import java.util.Map;


public interface UserService {

    Map<String, String> registerUser(RegisterRequest request);
    Map<String, String> authenticate(LoginRequest request);
    boolean updateRole(Long userId, Role role);
    boolean updateStatus(Long userId, Status status);
    UserDto getUserById(Long id);
    List<UserDto> getUsers();
    boolean changePassword(String oldPassword, String newPassword);
}
