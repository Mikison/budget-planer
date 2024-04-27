package pl.sonmiike.reportsservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserRoleEnum;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {


    @Mock
    private JwtUtil jwtUtil;


    @Mock
    private UserDetailsServiceClass userDetailsService;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;


    private Key key;

    private String validToken;


    @BeforeEach
    void setUp() {
        openMocks(this);

        String secretString = "MiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpP";
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        key = Keys.hmacShaKeyFor(keyBytes);
        validToken = buildToken(1000 * 120);
        
        
        
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validToken);
        response = new MockHttpServletResponse();

        JwtUtil.setStaticSecretKey(secretString);
        
    }

    @Test
    void whenNoAuthorizationHeader_thenContinueFilterChain() throws Exception {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void whenAuthorizationHeaderDoesNotStartWithBearer_thenContinueFilterChain() throws Exception {
        request.addHeader("Authorization", "Basic abcdefghijklmnopqrstuvwxyz");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testAuthenticationSuccessful() throws ServletException, IOException {
        UserDetails userDetails = getUser();
        String userName = userDetails.getUsername();

        try (MockedStatic<JwtUtil> jwtUtilMockedStatic = mockStatic(JwtUtil.class)) {
            jwtUtilMockedStatic.when(() -> JwtUtil.extractUsername(validToken)).thenReturn(userName);
            when(userDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);
            when(jwtUtil.isTokenValid(validToken, userDetails)).thenReturn(true);

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(userDetailsService).loadUserByUsername(userName);

            verify(jwtUtil).isTokenValid(validToken, userDetails);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(authentication, "Authentication should not be null");
            assertTrue(authentication.isAuthenticated(), "User should be authenticated");
            assertEquals(userDetails, authentication.getPrincipal(), "Authenticated user should be the expected one");

            verify(filterChain).doFilter(request, response);
        }


//        when(userDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);
//        when(jwtUtil.isTokenValid(validToken, userDetails)).thenReturn(true);
//        when(jwtUtil.extractUsername(validToken)).thenReturn(userName);
//
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//
//        verify(userDetailsService).loadUserByUsername(userName);
//
//        verify(jwtUtil).isTokenValid(validToken, userDetails);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        assertNotNull(authentication, "Authentication should not be null");
//        assertTrue(authentication.isAuthenticated(), "User should be authenticated");
//        assertEquals(userDetails, authentication.getPrincipal(), "Authenticated user should be the expected one");
//
//        verify(filterChain).doFilter(request, response);
    }



    @Test
    void whenAuthorizationHeaderWithNullUsername_thenContinueFilterChain() throws Exception {
        request.addHeader("Authorization", "Bearer " + validToken);

        try (MockedStatic<JwtUtil> jwtUtilMockedStatic = mockStatic(JwtUtil.class)) {
            jwtUtilMockedStatic.when(() -> JwtUtil.extractUsername(validToken)).thenReturn(null);

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }


    private UserDetails getUser() {
        return new CustomUserDetails(new UserReport(1L, "testUser", "test", "test@test.com", "password", UserRoleEnum.ROLE_USER));
    }

    private String buildToken(long expirationTime) {
        return Jwts.builder()
                .setSubject("test@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
