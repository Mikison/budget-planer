package pl.sonmiike.authenticationservice.serivce;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sonmiike.authenticationservice.service.JwtService;
import pl.sonmiike.authenticationservice.service.TokenType;

import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {


    private Clock fixedClock;


    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        jwtService = new JwtService(fixedClock);
        String secret = "ASUDJNASOID029840923840mKJIOSAJDIOASDASDASDASD";
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        String expiration = "3600";
        ReflectionTestUtils.setField(jwtService, "expiration", expiration);

    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        TokenType tokenType = TokenType.ACCESS_TOKEN;

        String token = jwtService.generateToken(username, tokenType);

        assertNotNull(token);

        Key key = jwtService.getSignKey();
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Claims tokenBody = claims.getBody();

        assertEquals(username, tokenBody.getSubject());
        assertNotNull(tokenBody.getIssuedAt());
        assertNotNull(tokenBody.getExpiration());
    }


    @Test
    public void testValidateToken() {
        String username = "testUser";
        TokenType tokenType = TokenType.ACCESS_TOKEN;
        String token = jwtService.generateToken(username, tokenType);

        assertDoesNotThrow(() -> jwtService.validateToken(token));
    }

    @Test
    public void testTokenExpiration() {
        String username = "expireTestUser";
        TokenType tokenType = TokenType.ACCESS_TOKEN;

        ReflectionTestUtils.setField(jwtService, "expiration", "1");
        String token = jwtService.generateToken(username, tokenType);

        Clock offsetClock = Clock.offset(fixedClock, Duration.ofSeconds(10));
        ReflectionTestUtils.setField(jwtService, "clock", offsetClock);


        Exception exception = assertThrows(RuntimeException.class, () -> jwtService.validateToken(token));
        assertTrue(exception.getMessage().contains("expired"));
    }
}
