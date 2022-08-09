package VetulusJava.Tribes.SecurityTest;

import VetulusJava.Tribes.Security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    // date of expiry is in the year 2030. It is a workaround to pass expiration validation.
    private final String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4";
    // date of expiration in 2019 // date of expiry is in the year 2030. It is a workaround to pass expiration validation.
    private final String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxNTYwNDYzNDgyLCJpYXQiOjE1NjAxMDM0ODJ9.AtLu7z2owgSzjBoEecLiKY_lUkjxZbVu4m5Mj0p9mYo";
    // token that has been manipulated
    private final String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5r";
    private final String validTokenName = "Bond";
    private final long validTokenExpirationDate = 1907618453;
    private JwtUtil jwtUtil;
    private static UserDetails userDetails = new User("Bond", "123", new ArrayList<>());

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    public void extractNameFromToken() {

        assertEquals(validTokenName, jwtUtil.extractUsername(validToken));
        assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(invalidToken));
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(expiredToken));
    }

    @Test
    public void extractExpirationDate() {
        assertEquals(validTokenExpirationDate, jwtUtil.extractExpiration(validToken).getTime() / 1000);// divide by 1000 to convert to seconds
        assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(invalidToken));
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(expiredToken));
    }

    @Test
    public void tokenIsGenerated() {
        assertNotNull(jwtUtil.generateToken(userDetails));
        String name = jwtUtil.extractUsername(jwtUtil.generateToken(userDetails)); // check whether the name used in creation can be retrieved correctly
        assertEquals("Bond", name);
    }

    @Test
    public void tokenValidation() {
        assertTrue(jwtUtil.validateToken(validToken, userDetails));
        assertThrows(SignatureException.class, () -> jwtUtil.validateToken(invalidToken, userDetails));
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.validateToken(expiredToken, userDetails));
    }

    @Test
    public void isTokenExpired() {
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isTokenExpired(expiredToken));
        assertFalse(jwtUtil.isTokenExpired(validToken));
    }

    @Test
    public void ExtractAllClaimsFromToken() {
        Claims claims = jwtUtil.extractAllClaims(validToken);
        String extractedName = claims.getSubject();
        Date extractedDateOfExpiry = claims.getExpiration();
        assertEquals(validTokenName, extractedName);
        assertEquals(new Date(validTokenExpirationDate * 1000), extractedDateOfExpiry);  //Date takes time in ms. The time in the token is in seconds.
    }
}