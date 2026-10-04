package com.mysite.sbb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

// 스프링 시큐리티 설정 클래스
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	// 모든 경로의 접근을 제어하기 위한 메소드
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> 
			// 스프링부트4 버전에서 변경됨 -> AntPathRequestMatcher에서 PassPatternRequestMatcher로 변경됨
			// 1. 프로젝트의 모든 경로를 허용함 (/**)
			// 2. /user/login 경로 허용
		    // 3. /user/logout 경로 허용
			authorizeHttpRequests.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/**")).permitAll())
			.formLogin((formLogin) -> formLogin.loginPage("/user/login").defaultSuccessUrl("/"))
			.logout((logout) -> logout
					.logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/user/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
			);
		return http.build();
	}
	
	// 비밀번호 암호화 메소드
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 사용자 인증을 처리하는 메소드
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager() ;
	}

}
