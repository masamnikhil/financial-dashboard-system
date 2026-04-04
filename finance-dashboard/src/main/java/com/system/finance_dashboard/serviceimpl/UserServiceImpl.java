package com.system.finance_dashboard.serviceimpl;

import com.system.finance_dashboard.security.JwtUtil;
import com.system.finance_dashboard.dto.*;
import com.system.finance_dashboard.entity.Role;
import com.system.finance_dashboard.entity.Status;
import com.system.finance_dashboard.entity.User;
import com.system.finance_dashboard.exception.PasswordUnmatchException;
import com.system.finance_dashboard.exception.UserAuthenticationException;
import com.system.finance_dashboard.exception.UsernameAlreadyExistsException;
import com.system.finance_dashboard.repository.UserRepository;
import com.system.finance_dashboard.service.UserService;
import com.system.finance_dashboard.util.PasswordGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> registerUser(RegisterRequest request) {

        boolean existingUsername = userRepository.existsByUsername(request.username().toLowerCase());
        if(existingUsername)
            throw new UsernameAlreadyExistsException("username already taken");

        boolean existingEmail = userRepository.existsByEmail(request.email().toLowerCase());
        if(existingEmail)
            throw new UsernameAlreadyExistsException("email already taken");

        try{
            String tempPassword = PasswordGenerator.generateTempPassword();
            User user = User.builder().name(request.name()).email(request.email().toLowerCase()).password(passwordEncoder.encode(tempPassword))
                    .username(request.username().toLowerCase()).role(request.role() == null ? Role.VIEWER : Role.valueOf(request.role())).build();

            User savedUser = userRepository.saveAndFlush(user);

            return Map.of("username", savedUser.getUsername(), "Temporary password", tempPassword, "caution", "Create new Password");

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> authenticate(LoginRequest request) {

        User user = userRepository.findByUsername(request.username().toLowerCase())
                .orElseThrow(() -> new UserAuthenticationException("invalid username or password"));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username().toLowerCase(), request.password()));

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().toString());
            return Map.of("accessToken", accessToken);

        }
        catch(DisabledException ex){
                   throw new UserAuthenticationException("Account is deactivated, please contact Admin");
        }
        catch (AuthenticationException ex){
             throw new UserAuthenticationException("Invalid username or password");
        }


    }

    @Override
    public boolean updateRole(Long userId, Role role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not exist"));

        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateStatus(Long userId, Status status) {
        User user = findUserById(userId);

        user.setStatus(status);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = findUserById(id);
        return mapToResponse(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        boolean passwordMatch = passwordEncoder.matches(oldPassword, user.getPassword());
        if(!passwordMatch)
            throw new PasswordUnmatchException("Incorrect old password");

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be same as old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private UserDto mapToResponse(User user) {
        UserDto response = UserDto.builder().id(user.getId()).username(user.getUsername()).name(user.getName())
                .email(user.getEmail()).status(user.getStatus().toString()).role(user.getRole().toString()).build();
        return response;
    }
}
