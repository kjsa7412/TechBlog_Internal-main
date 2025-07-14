package com.SJTB.framework.security;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.utils.FrameStringUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT 토큰을 생성하고 유효성을 검증하는 컴포넌트 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    private String secretKey = FrameConstants.JWT_SECRET_KEY;
    private String refreshSecretKey = FrameConstants.JWT_REFRESH_SECRET_KEY;

    /**
     * 초기화 메서드입니다. Base64 인코딩을 수행하여 비밀 키를 초기화합니다.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 사용자 ID와 권한을 기반으로 Access Token을 생성합니다.
     *
     * @param userId 사용자 ID
     * @param userAuth 사용자 권한
     * @return 생성된 Access Token
     */
    public String createAccessToken(String userId, String userAuth) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("auth", userAuth);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + FrameConstants.JWT_ACCESS_TOKEN_VALID_TIME)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘 및 비밀 키 설정
                .compact();

        return accessToken;
    }

    /**
     * 사용자 Id와 권한을 기반으로 Refresh Token을 생성합니다.
     *
     * @param userId 사용자 ID
     * @param userAuth 사용자 권한
     * @return 생성된 Refresh Token
     */
    public String createRefreshToken(String userId, String userAuth) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("auth", userAuth);
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + FrameConstants.JWT_REFRESH_TOKEN_VALID_TIME)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)  // 암호화 알고리즘 및 비밀 키 설정
                .compact();

        return refreshToken;
    }

    /**
     * 주어진 토큰으로부터 사용자 인증 정보를 추출하여 Authentication 객체를 반환합니다.
     *
     * @param token 토큰
     * @return Authentication 객체
     * @throws Exception 예외 발생 시
     */
    public Authentication getAuthentication(String token) throws Exception {
        // parseClaims 메소드를 통해 string 형의 토큰을 claims 형으로 변경
        Claims claims = parseClaims(token);

        // 만약 토큰의 정보가 들어간 claims에 auth가 없을 경우 예외를 반환
        if (claims.get("auth") == null) {
            throw new AccessDeniedException("권한 정보가 없는 토큰입니다.");
        }

        // GrantedAuthority을 상속받은 타입만이 사용 가능한 Collection을 반환하는데
        // stream을 통한 함수형 프로그래밍으로 claims 형의 토큰을 정렬한 후
        // GrantedAuthority을 상속받은 SimpleGrantedAuthority 형의 인가가 들어간 List를 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // Spring Security의 유저 정보를 담는 인터페이스인 UserDetails에 token에서 얻은 정보와, 아까 생성한 인증정보를 넣음
        UserDetails principal = new User(this.getUserIdFromAccessToken(token), "", authorities);

        // 유저 정보와 인가 정보를 UsernamePasswordAuthenticationToken에 넣고 반환
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * Access Token으로부터 사용자 ID를 추출합니다.
     *
     * @param token Access Token
     * @return 사용자 ID
     */
    public String getUserIdFromAccessToken(String token) {
        if (FrameStringUtil.isEmpty(token)) {
            return null;
        }

        try {
            // 만료되지 않은 토큰의 경우
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 경우: 서명을 검증하지 않고 클레임에서 사용자 ID 추출
            Claims claims = e.getClaims(); // ExpiredJwtException에서 Claims를 추출할 수 있음
            return claims.getSubject();    // 사용자 ID 추출
        }
    }

    /**
     * Refresh Token으로부터 사용자 ID를 추출합니다.
     *
     * @param token Refresh Token
     * @return 사용자 ID
     */
    public String getUserIdFromRefreshToken(String token) {
        if (FrameStringUtil.isEmpty(token)) {
            return null;
        }

        try {
            // 만료되지 않은 토큰의 경우
            return Jwts.parser()
                    .setSigningKey(refreshSecretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 경우: 서명을 검증하지 않고 클레임에서 사용자 ID 추출
            Claims claims = e.getClaims(); // ExpiredJwtException에서 Claims를 추출할 수 있음
            return claims.getSubject();    // 사용자 ID 추출
        }
    }

    /**
     * HTTP 요청에서 Access Token을 추출합니다.
     *
     * @param request HTTP 요청
     * @return 추출된 토큰
     */
    public String resolveAccessToken(HttpServletRequest request) {
        // Authorization 헤더 값 추출
        String authorizationHeader = request.getHeader("Authorization");
        String token = "";

        // 헤더에 존재하는 토큰이 올바른 토큰인 경우 값 할당
        if (authorizationHeader != null && authorizationHeader.startsWith(FrameConstants.JWT_BEARER_PREFIX)) {
            token = authorizationHeader.substring(7);
        }

        return token;
    }

    /**
     * HTTP 요청에서 RefreshToken을 추출합니다.
     *
     * @param request HTTP 요청
     * @return 추출된 토큰
     */
    public String resolveRefreshToken(HttpServletRequest request) {
        // RefreshToken은 쿠키에 저장되어 있음
        // Request Header의 내용중 쿠키의 값들을 추출
        Cookie[] Cookies = request.getCookies();
        String token = "";

        // 쿠키가 없을 경우 예외처리
        if (Cookies == null) {
            return token;
        };

        // 토큰의 값을 추출 후 리턴
        for (Cookie cookie : Cookies) {
            if (cookie.getName().equals(FrameConstants.JWT_REFRESH_TOKEN_NAME)) {
                token = cookie.getValue();
            }
        }

        return token;
    }

    /**
     * 주어진 AccessToken의 유효성을 검사합니다.
     *
     * @param token AccessToken
     * @return 유효성 검사 결과 (유효한 토큰인 경우 true, 그렇지 않은 경우 false)
     */
    public boolean validateAccessToken(HttpServletRequest request, String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) { // 토큰이 만료된 경우
            request.setAttribute("exception", FrameConstants.JWT_EXPIRED_TOKEN_MSG);
        } catch (Exception e) { // 그 이외의 경우
            request.setAttribute("exception", FrameConstants.JWT_INVALID_TOKEN_MSG);
        }

        return false;
    }

    /**
     * 주어진 RefreshToken의 유효성을 검사합니다.
     *
     * @param token RefreshToken
     * @return 유효성 검사 결과 (유효한 토큰인 경우 true, 그렇지 않은 경우 false)
     */
    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.debug("[validateToken] 예외발생: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Access Token을 claims 형으로 변경합니다.
     *
     * @param accessToken Access Token
     * @return claims형 Access Token
     */
    private Claims parseClaims(String accessToken) {
        // 파라미터 accessToken의 정보를 통해 claims 형의 토큰 생성
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        }
        // Jwt의 지정된 유효기간 초과할 때 사용되는 예외처리 (auth가 없는 claims를 리턴)
        catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}