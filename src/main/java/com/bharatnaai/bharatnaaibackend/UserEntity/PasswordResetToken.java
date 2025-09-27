package com.bharatnaai.bharatnaaibackend.UserEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

    @Entity
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public class PasswordResetToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String token;

        @OneToOne
        private User user;

        @Column(nullable = false)
        private LocalDateTime expiryDate;
    }


