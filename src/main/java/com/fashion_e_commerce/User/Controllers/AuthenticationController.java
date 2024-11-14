package com.fashion_e_commerce.User.Controllers;

import com.fashion_e_commerce.User.Components.JwtTokenUtil;
import com.fashion_e_commerce.User.Entities.LoginRequest;
import com.fashion_e_commerce.User.Entities.User;
import com.fashion_e_commerce.User.Services.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private UserDataService userDataService; // Change to UserDataService for registration and login
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String response = userDataService.register(user);
        return ResponseEntity.ok(response);
    }

    // Log in an existing user and return JWT token
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userDataService.login(loginRequest.getEmail(), loginRequest.getPassword());
            // Retrieve the user object
            User user = userDataService.getUserByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(user.getId())); // add userId to response

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
    }

}
