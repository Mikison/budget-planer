package pl.sonmiike.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sonmiike.authenticationservice.DTO.AuthResponse;
import pl.sonmiike.authenticationservice.DTO.RegisterRequest;
import pl.sonmiike.authenticationservice.entity.RoleEnum;
import pl.sonmiike.authenticationservice.entity.UserCredential;
import pl.sonmiike.authenticationservice.repository.UserCredentialRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse saveUser( RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }
        UserCredential userCredential = UserCredential.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(RoleEnum.ROLE_USER)
                .build();
        userRepository.save(userCredential);
        return new AuthResponse(
                generateToken(registerRequest.getEmail(), TokenType.ACCESS_TOKEN),
                generateToken(registerRequest.getEmail(), TokenType.REFRESH_TOKEN));
    }

    public String generateToken(String username, TokenType tokenType) {
        return jwtService.generateToken(username, tokenType);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}