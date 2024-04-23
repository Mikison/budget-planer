package pl.sonmiike.authenticationservice.Cotnroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.authenticationservice.DTO.AuthRequest;
import pl.sonmiike.authenticationservice.DTO.AuthResponse;
import pl.sonmiike.authenticationservice.DTO.RegisterRequest;
import pl.sonmiike.authenticationservice.Service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> addNewUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(service.saveUser(registerRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.ok(new AuthResponse(service.generateToken(authRequest.getEmail(), "ACCESS"), service.generateToken(authRequest.getEmail(), "REFRESH")));
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }
}