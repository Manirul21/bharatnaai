package com.bharatnaai.bharatnaaibackend.UserRepository;
import com.bharatnaai.bharatnaaibackend.UserEntity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

