package pl.sonmiike.authenticationservice.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sonmiike.authenticationservice.DTO.AuthResponse;
import pl.sonmiike.authenticationservice.DTO.RegisterRequest;
import pl.sonmiike.authenticationservice.Entity.RoleEnum;
import pl.sonmiike.authenticationservice.Entity.UserCredential;
import pl.sonmiike.authenticationservice.Repository.UserCredentialRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse saveUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }
        UserCredential userCredential = UserCredential.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(RoleEnum.ROLE_USER)
                .build();
        userRepository.save(userCredential);
        return new AuthResponse(generateToken(registerRequest.getEmail(),"ACCESS"), generateToken(registerRequest.getEmail(), "REFRESH") );
    }

    public String generateToken(String username, String tokenType) {
        return jwtService.generateToken(username, tokenType);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}