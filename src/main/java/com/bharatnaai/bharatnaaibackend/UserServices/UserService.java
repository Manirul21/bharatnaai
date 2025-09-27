package com.bharatnaai.bharatnaaibackend.UserServices;
import com.bharatnaai.bharatnaaibackend.UserEntity.User;
import com.bharatnaai.bharatnaaibackend.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // must be encoded in DB
                .authorities(
                        user.getRoles().stream()
                                .map(r -> "ROLE_" + r.getName()) // ensure correct prefix
                                .toArray(String[]::new)
                )
                .build();
    }
}