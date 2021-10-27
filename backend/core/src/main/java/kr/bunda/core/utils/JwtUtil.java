package kr.bunda.core.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class JwtUtil {

    /**
     * Jwt Claim 정보
     *
     * @param secret : secret key
     * @param token  : token
     * @return : Claims to Map
     * @throws IllegalArgumentException : invalid token
     * @throws IllegalArgumentException : invalid secret
     */
    public static Map<String, ?> getClaims(String secret, String token) {
        Objects.requireNonNull(secret);
        Objects.requireNonNull(token);
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build();
            Jws<Claims> jws = parser.parseClaimsJws(token);
            return jws.getBody();
        } catch (ExpiredJwtException e) {
            log.warn("token expired , token" + token);
            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("token invalid, token : " + token);
        }
    }
}