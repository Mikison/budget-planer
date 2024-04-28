package pl.sonmiike.authenticationservice.serivce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sonmiike.authenticationservice.DTO.AuthResponse;
import pl.sonmiike.authenticationservice.DTO.RegisterRequest;
import pl.sonmiike.authenticationservice.repository.UserCredentialRepository;
import pl.sonmiike.authenticationservice.service.AuthService;
import pl.sonmiike.authenticationservice.service.JwtService;
import pl.sonmiike.authenticationservice.service.TokenType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


public class AuthServiceTest {

    @Mock
    private UserCredentialRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void testSaveUser_WhenEmailIsAlreadyTaken() {
        RegisterRequest registerRequest = getRegisterRequest();
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.saveUser(registerRequest));


    }

    @Test
    void testSaveUser_WhenEmailIsNotTaken() {
        RegisterRequest registerRequest = getRegisterRequest();

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(registerRequest.getEmail(), TokenType.ACCESS_TOKEN)).thenReturn("access_token");
        when(jwtService.generateToken(registerRequest.getEmail(), TokenType.REFRESH_TOKEN)).thenReturn("refresh_token");

        AuthResponse authResponse = authService.saveUser(registerRequest);

        assertEquals("access_token", authResponse.getAccessToken());
        assertEquals("refresh_token", authResponse.getRefreshToken());
        verify(userRepository, times(1)).save(any());
        verify(jwtService, times(2)).generateToken(any(), any());


    }

    @Test
    void testGenerateToken_ReturnsToken() {
        String email = getRegisterRequest().getEmail();

        when(authService.generateToken(email, TokenType.ACCESS_TOKEN)).thenReturn("token");

        String token = authService.generateToken(email, TokenType.ACCESS_TOKEN);

        assertEquals("token", token);
        verify(jwtService, times(1)).generateToken(any(), any());
    }

    @Test
    void testValidateToken() {
        String token = "token";

        authService.validateToken(token);
        verify(jwtService, times(1)).validateToken(token);
    }



    private RegisterRequest getRegisterRequest() {
        return new RegisterRequest("test", "tester", "test@test.com", "test123");

    }
}
