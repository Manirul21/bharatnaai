package com.bharatnaai.bharatnaaibackend.UserController;

import com.bharatnaai.bharatnaaibackend.UserEntity.User;
import com.bharatnaai.bharatnaaibackend.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")  // all endpoints start with /users
@RequiredArgsConstructor
public class UserAuthController {

    private final UserRepository userRepository;

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
