package kr.bunda.user.security;

import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.bunda.user.dto.TokenModel;
import kr.bunda.user.dto.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private final Key accessKey;
    private final Key refreshKey;
    private final Long accessExpireTime;
    private final Long refreshExpireTime;

    public TokenProvider(@Value("${bunda.token.access.secret}") String accessSecretKey,
                         @Value("${bunda.token.refresh.secret}") String refreshSecretKey,
                         @Value("${bunda.token.access.expired:1800}") String accessExpired,
                         @Value("${bunda.token.refresh.expired:604800}") String refreshExpired) {

        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.accessExpireTime = Long.parseLong(accessExpired);
        this.refreshExpireTime = Long.parseLong(refreshExpired);
    }


    public Authentication getAuthentication(String accessToken) {
        
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority((String) claims.get(AUTHORITIES_KEY));
        Collection<GrantedAuthority> authorities = Lists.newArrayList(authority);
        return new UsernamePasswordAuthenticationToken(claims.get("email"), "", authorities);
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 토큰입니다. token : " + accessToken);
        }
    }


    private String generateAccessToken(UserModel.UserDto user, Date issuedDate, Date expiredDate) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(issuedDate);
        Objects.requireNonNull(expiredDate);
        try {

            Map<String, Object> claimMap = new HashMap<>();
            claimMap.put("email", user.getEmail());
            claimMap.put("name", user.getName());
            claimMap.put("id", user.getId());
            claimMap.put("auth", user.getAuth().name());

            Claims claims = Jwts.claims(claimMap);

            return Jwts.builder().setClaims(claims).setIssuedAt(issuedDate).setExpiration(expiredDate).signWith(accessKey, SignatureAlgorithm.HS512).compact();
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to create token, message : " + e.getMessage() + "user : " + user);
        }
    }

    private String generateRefreshToken(UserModel.UserDto user, String ipAddress, Date issuedDate, Date expiredDate) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(ipAddress);
        Objects.requireNonNull(issuedDate);
        Objects.requireNonNull(expiredDate);

        try {
            Map<String, Object> claimMap = new HashMap<>();
            claimMap.put("ipAddress", ipAddress);
            claimMap.put("id", user.getId());
            Claims claims = Jwts.claims(claimMap);
            return Jwts.builder().setClaims(claims).setIssuedAt(issuedDate).setExpiration(expiredDate).signWith(refreshKey, SignatureAlgorithm.HS512).compact();
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to create token,user : " + user);
        }
    }

    public TokenModel.TokenDto generate(UserModel.UserDto user, String ipAddress) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(ipAddress);

        Instant now = Instant.now();
        Date issuedDate = Date.from(now);
        Date accessExpiredDate = Date.from(now.plusSeconds(accessExpireTime));
        Date refreshExpiredDate = Date.from(now.plusSeconds(refreshExpireTime));

        String accessToken = generateAccessToken(user, issuedDate, accessExpiredDate);
        String refreshToken = generateRefreshToken(user, ipAddress, issuedDate, refreshExpiredDate);

        return TokenModel.TokenDto.builder()
                .accessToken(accessToken)
                .accessExpires(accessExpiredDate.toString())
                .refreshToken(refreshToken)
                .refreshExpires(refreshExpiredDate.toString())
                .build();
    }

    public TokenModel.TokenDto generate(UserModel.UserDto user) {
        Objects.requireNonNull(user);

        Instant now = Instant.now();
        Date issuedDate = Date.from(now);
        Date accessExpiredDate = Date.from(now.plusSeconds(accessExpireTime));

        String accessToken = generateAccessToken(user, issuedDate, accessExpiredDate);

        return TokenModel.TokenDto.builder()
                .accessToken(accessToken)
                .accessExpires(accessExpiredDate.toString())
                .build();
    }

}