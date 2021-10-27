package kr.bunda.user.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * TokenAuthenticationFilter 토큰 기반 인증 방식을 사용한 필터
 *
 * @author BeomHee Han
 * @since 2021-08-18
 **/
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public static String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    TokenAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = request.getHeader(AUTHORIZATION_HEADER);
            if (token != null) {
                if (token.startsWith("Bearer ") || token.startsWith("Bearer ")) {
                    Authentication authentication = tokenProvider.getAuthentication(token.substring(7));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 토큰 요청", e);
        }
        filterChain.doFilter(request, response);
    }
}