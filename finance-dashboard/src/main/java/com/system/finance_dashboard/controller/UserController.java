package com.system.finance_dashboard.controller;

import com.system.finance_dashboard.dto.LoginRequest;
import com.system.finance_dashboard.dto.RegisterRequest;
import com.system.finance_dashboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequest request){
        Map<String, String> tokens = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> userLogin(@Valid @RequestBody LoginRequest request){
        Map<String, String> tokens = userService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @PreAuthorize("hasRole('VIEWER')")
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.status(HttpStatus.OK).body("Hello World");
    }
}
