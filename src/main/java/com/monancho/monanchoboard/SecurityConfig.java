package com.monancho.monanchoboard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            
            .formLogin((formLogin) -> formLogin //스프링 시큐리티에서 로그인 설정
            		.loginPage("/user/login") //로그인 요청
            		.defaultSuccessUrl("/")) //로그인 성공 시 이동할 페이지 루트로 지정
            .logout((logout) -> logout
            		.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
            		.logoutSuccessUrl("/")
            		.invalidateHttpSession(true))
            
            		
            ;
        return http.build();
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration
			authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
			

}
