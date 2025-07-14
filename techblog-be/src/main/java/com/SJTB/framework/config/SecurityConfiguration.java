package com.SJTB.framework.config;

import com.SJTB.framework.security.CustomAccessDeniedHandler;
import com.SJTB.framework.security.CustomAuthenticationEntryPoint;
import com.SJTB.framework.security.JwtAuthenticationFilter;
import com.SJTB.framework.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스입니다.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * HTTP 요청에 대한 보안 설정을 구성합니다.
     *
     * @param http HttpSecurity 객체
     * @throws Exception 예외
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable() // CORS 설정과 CSRF 보호를 비활성화 (HTTP Basic 인증도 함께 자동으로 비활성)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 정책 설정 (세션을 사용하지 않음)
                .and()
                .authorizeRequests() // 요청에 대한 인가 설정 시작
                .antMatchers(("/favicon.ico")).permitAll()
                .antMatchers("/public/**").permitAll() // public 으로 시작하는 요청은 접근 허용
//                .antMatchers("/private/**").permitAll() // public 으로 시작하는 요청은 접근 허용
                .anyRequest().authenticated()
                .and()
                .exceptionHandling() // 예외 처리 설정 시작
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 접근 거부 예외 처리
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 예외 처리
                .and()
                // cors 관련 필터 적용(전역 설정)
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이전에 등록
    }
}
