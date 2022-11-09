package com.connect.refugee.connectrefugee.controllers;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.connect.refugee.connectrefugee.models.User;
import com.connect.refugee.connectrefugee.payload.request.*;
import com.connect.refugee.connectrefugee.payload.response.*;
import com.connect.refugee.connectrefugee.repositories.AuthRepository;
import com.connect.refugee.connectrefugee.jwt.JwtUtil;
import com.connect.refugee.connectrefugee.jwt.services.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername()));            
        }catch(Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid username or password!"));
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User signUpRequest) {
        if (authRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username is already taken!"));
        }
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));
        authRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        String token = accessToken.replace("Bearer ", "");
        if (!jwtUtil.validateJwtToken(token)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid Token!"));
        }
        jwtUtil.expireJwtToken(token);
        return ResponseEntity.ok(new MessageResponse("Logged out"));
    }

    @PostMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@Valid @RequestBody String username) {
        if (authRepository.existsByUsernameIgnoreCase(username)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}
