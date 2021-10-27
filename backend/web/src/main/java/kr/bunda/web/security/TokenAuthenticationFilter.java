package kr.bunda.web.security;

import kr.bunda.core.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * TokenAuthenticationFilter 토큰 기반 인증 방식을 사용한 필터
 *
 * @author BeomHee Han
 * @since 2021-08-18
 **/
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final String accessSecret;
    private final String loginUrl;
    private final String refreshUrl;

    TokenAuthenticationFilter(String accessSecret, String loginUrl, String refreshUrl) {
        this.accessSecret = accessSecret;
        this.loginUrl = loginUrl;
        this.refreshUrl = refreshUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String requestUrl = request.getRequestURI();

            String token = request.getHeader("Authorization");
            if (!ObjectUtils.isEmpty(token)) {
                if (token.startsWith("Bearer ")) {
                    Map<String, ?> claims = JwtUtil.getClaims(accessSecret, token.substring(7));
                    if (claims != null) {
                        String id = (String) claims.get("id");
                        String role = (String) claims.get("role");
                        if (!ObjectUtils.isEmpty(id) && !ObjectUtils.isEmpty(role)) {
                            List<GrantedAuthority> auth = new ArrayList<>();
                            auth.add(new SimpleGrantedAuthority("ROLE_" + role));
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, null,
                                    auth);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    } else {
                        //만료
                        
                    }

                }
            }


        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}