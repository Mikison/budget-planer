package pl.sonmiike.reportsservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class JwtUtilTest {

    private final String username = "test@test.com";
    @InjectMocks
    private JwtUtil jwtUtil;
    @Mock
    private UserDetails userDetails;
    private String validToken = "";
    private String expiredToken = "";
    private Key key;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        String secretString = "MiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpPMiAVzqUXy5Tfr1kVIGpP";
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        key = Keys.hmacShaKeyFor(keyBytes);

        JwtUtil.setStaticSecretKey(secretString);

        validToken = buildToken(1000 * 120);
        expiredToken = buildToken(-1000 * 120);
    }

    @Test
    public void testExtractUsername() {
        ReflectionTestUtils.setField(jwtUtil, "STATIC_SECRET_KEY", Base64.getEncoder().encodeToString(key.getEncoded()));

        String extractedUsername = JwtUtil.extractUsername(validToken);
        assertEquals(username, extractedUsername);
    }


    @Test
    public void testIsTokenValid_withValidToken() {
        when(userDetails.getUsername()).thenReturn(username);
        assertTrue(jwtUtil.isTokenValid(validToken, userDetails));
    }

    @Test
    public void testIsTokenValid_withExpiredToken() {
        when(userDetails.getUsername()).thenReturn(username);
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isTokenValid(expiredToken, userDetails));
    }


    @Test
    public void testIsTokenValid_withMalformedToken() {
        String malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        when(userDetails.getUsername()).thenReturn(username);
        assertThrows(MalformedJwtException.class, () -> jwtUtil.isTokenValid(malformedToken, userDetails));
    }


    @Test
    public void testIsTokenValid_withNullToken() {
        String nullToken = null;
        when(userDetails.getUsername()).thenReturn(username);
        assertThrows(IllegalArgumentException.class, () -> jwtUtil.isTokenValid(nullToken, userDetails));
    }

    @Test
    public void testIsTokenValid_withEmptyToken() {
        String emptyToken = "";
        when(userDetails.getUsername()).thenReturn(username);
        assertThrows(IllegalArgumentException.class, () -> jwtUtil.isTokenValid(emptyToken, userDetails));
    }


    @Test
    public void testIsTokenValid_justExpiredToken() {
        long now = System.currentTimeMillis();
        String justExpiredToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now - 1000 * 60 * 5))
                .setExpiration(new Date(now - 1))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        when(userDetails.getUsername()).thenReturn(username);
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isTokenValid(justExpiredToken, userDetails));
    }


    @Test
    public void testIsTokenValid_expiresNowToken() {
        long now = System.currentTimeMillis();
        String expiresNowToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now - 1000 * 60 * 5))
                .setExpiration(new Date(now))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        when(userDetails.getUsername()).thenReturn(username);
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isTokenValid(expiresNowToken, userDetails));
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