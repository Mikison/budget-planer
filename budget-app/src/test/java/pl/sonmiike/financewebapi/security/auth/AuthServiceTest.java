package pl.sonmiike.financewebapi.security.auth;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sonmiike.financewebapi.user.UserEntity;
import pl.sonmiike.financewebapi.user.UserRepository;
import pl.sonmiike.financewebapi.user.refreshToken.RefreshTokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() throws Exception {
        openMocks.close();
    }


    @Test
    void whenGetUserId_thenSucceeds() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(userEntity);

        Long userId = authService.getUserId(authentication);

        assertNotNull(userId);
        assertEquals(Long.valueOf(1L), userId);
        assertEquals(userEntity, authentication.getPrincipal());
    }

}
