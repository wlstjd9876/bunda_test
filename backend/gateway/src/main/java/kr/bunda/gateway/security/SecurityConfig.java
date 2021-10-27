package kr.bunda.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public ReactiveUserDetailsService userDetailsService(@Value("${bunda.gateway.id}") String id, @Value("${bunda.gateway.password}") String pwd) {
        UserDetails user = User.withDefaultPasswordEncoder().username(id)
                .password(pwd)
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/**").permitAll()
                .pathMatchers("/actuator/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
        ;
        return http.build();
    }

}