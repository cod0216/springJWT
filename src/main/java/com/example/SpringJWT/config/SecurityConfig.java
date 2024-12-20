package com.example.SpringJWT.config;

import com.example.SpringJWT.jwt.JWTFilter;
import com.example.SpringJWT.jwt.JWTUtil;
import com.example.SpringJWT.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;

    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws  Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3030"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);

                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));


        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());


        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());


        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //At 원하는 자리 before 해당하는 필터 전에 after 해당하는 필터 이후에
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정 스테이리스 상테로 설정 (중요함)
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }


    /*
    스프링 시큐리티는 클라이언트의 요청이 여러개의 필터를 거친 DispatcherServlet(Controller)으로 향하는 중간 필터에서 요청을 가로챈 후 거믕(인증/인가)
    를 진행한다.

    클라이언트 요청 -> 서블릿 필터 -> 서블릿(컨트롤러)
    DlelgatingFilterProxy -> 시큐리티 필터 체인 (시큐리티 필터의 모음)

    필터의 목록과 순서도 있음 ( 매우 많음 )
    From 로그인 방식에서는 UsernamePassWordAuthenticaitonfilter


    2회차때 강의 내용 보면서 다시 정리하고
    결과적으로 로그인을 진행하기 위해서 필터를 커스텀 등록해야 한다. 왜? 폼 방식이 disable이기 때문

    로긘 로직 구현 목표
    아이디, 비밀번호, 검증을 위한 커스텀 필터 작성
    DB에 저장되어ㅣ 있는 회원정보를 기반으로 검증할 로직 작성
    로그인 성공시 JWT를 반환할 Success 핸들러 생성
    커스텀 필터 SecurityConfig에 등록

     */
}
