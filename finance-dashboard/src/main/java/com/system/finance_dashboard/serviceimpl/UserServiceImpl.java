package com.system.finance_dashboard.serviceimpl;

import com.system.finance_dashboard.config.JwtUtil;
import com.system.finance_dashboard.dto.LoginRequest;
import com.system.finance_dashboard.dto.RegisterRequest;
import com.system.finance_dashboard.entity.Role;
import com.system.finance_dashboard.entity.User;
import com.system.finance_dashboard.exception.UserAuthenticationException;
import com.system.finance_dashboard.exception.UsernameAlreadyExistsException;
import com.system.finance_dashboard.repository.UserRepository;
import com.system.finance_dashboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        boolean existingUser = userRepository.existsByUsername(request.username().toLowerCase());
        if(existingUser)
            throw new UsernameAlreadyExistsException("username already taken");

        try{
            User user = User.builder().name(request.name()).email(request.email().toLowerCase()).password(passwordEncoder.encode(request.password()))
                    .username(request.username().toLowerCase()).role(Role.VIEWER).build();

            User savedUser = userRepository.saveAndFlush(user);
            String accessToken = jwtUtil.generateAccessToken(savedUser.getUsername(), savedUser.getRole().toString());
            String refreshToken = jwtUtil.generateRefreshToken(savedUser.getUsername(), savedUser.getRole().toString());
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> authenticate(LoginRequest request) {

        User user = userRepository.findByUsername(request.username().toLowerCase())
                .orElseThrow(() -> new UserAuthenticationException("invalid username"));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username().toLowerCase(), request.password()));

            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().toString());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole().toString());
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        }
        catch(DisabledException ex){
                   throw new UserAuthenticationException("Account is inactive, contact Admin");
        }
        catch (AuthenticationException ex){
             throw new UserAuthenticationException(ex.getMessage());
        }


    }
}
