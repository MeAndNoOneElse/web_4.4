package service;

import dto.LoginRequest;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String register(LoginRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        String hash = passwordEncoder.encode(request.getPassword());

        // authProvider можно держать как "LOCAL"
        User user = new User(request.getUsername(), hash, "LOCAL");
        userRepository.save(user);  // <<< использование репозитория

        return jwtService.generateToken(user.getUsername());
    }

    public String login(LoginRequest request) {
        User user = (User) userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")); // <<< использование репозитория

        if (user.getPasswordHash() == null ||
                !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getUsername());
    }
}
