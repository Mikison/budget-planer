package pl.sonmiike.authenticationservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.authenticationservice.DTO.AuthRequest;
import pl.sonmiike.authenticationservice.DTO.AuthResponse;
import pl.sonmiike.authenticationservice.DTO.RegisterRequest;
import pl.sonmiike.authenticationservice.service.AuthService;
import pl.sonmiike.authenticationservice.service.TokenType;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> addNewUser(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.saveUser(registerRequest));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.ok(new AuthResponse(authService.generateToken(authRequest.getEmail(), TokenType.ACCESS_TOKEN), authService.generateToken(authRequest.getEmail(), TokenType.REFRESH_TOKEN)));
        } else {
            throw new RuntimeException("Authentication Failed");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }
}