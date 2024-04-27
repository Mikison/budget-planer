package pl.sonmiike.budgetapp.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sonmiike.budgetapp.user.UserEntity;
import pl.sonmiike.budgetapp.user.UserRole;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;

    private final String SECRET_KEY = "MiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpP";
    private final UserEntity USER_DETAILS = UserEntity.builder()
            .userId(1L)
            .email("test@test.com")
            .username("testUser")
            .role(UserRole.ROLE_USER)
            .build();


    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        jwtService.setSecretKey(SECRET_KEY);
        jwtService.init();
    }

    @Test
    void whenGenerateToken_thenSuccess() {
        String token = jwtService.generateToken(USER_DETAILS);

        assertTrue(token != null && !token.isEmpty());
    }

    @Test
    void whenTokenIsValid_thenSuccess() {
        String token = jwtService.generateToken(USER_DETAILS);
        boolean isValid = jwtService.isTokenValid(token, USER_DETAILS);

        assertTrue(isValid);
    }

    @Test
    void whenTokenIsInvalid_thenFailure() {
        UserEntity anotherUserDetails = UserEntity.builder()
                .userId(2L)
                .email("test2@test.com")
                .username("testUser2")
                .role(UserRole.ROLE_USER)
                .build();
        String token = jwtService.generateToken(USER_DETAILS);

        boolean isValid = jwtService.isTokenValid(token, anotherUserDetails);

        assertFalse(isValid);
    }

}