package com.system.finance_dashboard.controller;

import com.system.finance_dashboard.dto.*;
import com.system.finance_dashboard.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequest request){
        Map<String, String> tokens = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
    }

    @SecurityRequirement(name = "")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> userLogin(@Valid @RequestBody LoginRequest request){
        Map<String, String> tokens = userService.authenticate(request);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePassRequest request){
        if(userService.changePassword(request.oldPassword(), request.newPassword()))
            return ResponseEntity.ok("Password changed successfully, log in again");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<String> updateRoleOfUser(@PathVariable("id")
                                                       @NotNull(message = "userId is required")
                                                       @Positive(message = "userId should be positive")
                                                       Long userId,
                                                       @Valid @RequestBody RoleUpdateRequest request){
           if(userService.updateRole(userId, request.role()))
               return ResponseEntity.status(HttpStatus.OK).body("Role successfully updated to " + request.role());

           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatusOfUser(@PathVariable("id")
                                                         @NotNull(message = "userId is required")
                                                         @Positive(message = "userId should be positive")
                                                         Long userId,
                                                     @Valid @RequestBody StatusUpdateRequest request){
        if(userService.updateStatus(userId, request.status()))
            return ResponseEntity.status(HttpStatus.OK).body("Status successfully updated to " + request.status());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id")
                                                   @NotNull(message = "userId is required")
                                                   @Positive(message = "userId should be positive")
                                                   Long userId
                                                     ){
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());

    }

}
