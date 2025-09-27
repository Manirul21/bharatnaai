package com.bharatnaai.bharatnaaibackend.UserController;
import com.bharatnaai.bharatnaaibackend.UserDto.*;
import com.bharatnaai.bharatnaaibackend.UserEntity.User;
import com.bharatnaai.bharatnaaibackend.UserEntity.Role;
import com.bharatnaai.bharatnaaibackend.UserRepository.UserRepository;
import com.bharatnaai.bharatnaaibackend.UserServices.JwtService;
import com.bharatnaai.bharatnaaibackend.UserServices.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;


import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;




    // Register
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered!";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.builder().name("CUSTOMER").build()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // 1. Check if user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password (user not found)"));
        }

//        // 2. Debug logs for password check
//        System.out.println(">>> LOGIN ATTEMPT <<<");
//        System.out.println("RAW password: " + request.getPassword());
//        System.out.println("ENCODED (DB): " + user.getPassword());
//        System.out.println("MATCHES? " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        // 3. Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password (wrong password)"));
        }

        // 4. Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        // 5. Return success response
        return ResponseEntity.ok(Map.of(
                "message", "Successfully login",
                "token", token
        ));
    }
    // Forgot password → return token in JSON
    @PostMapping("/forgot-password")
    public PasswordResetTokenResponse forgotPassword(@RequestBody PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        var token = passwordResetService.createToken(user);

        // TODO: send token via email in production
        return new PasswordResetTokenResponse(user.getEmail(), token.getToken());
    }

    // Reset password using token
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetDto resetDto) {
        User user = passwordResetService.validateToken(resetDto.getToken());
        user.setPassword(passwordEncoder.encode(resetDto.getNewPassword()));
        userRepository.save(user);

        passwordResetService.deleteToken(resetDto.getToken());

        return "Password has been reset successfully!";
    }
}
