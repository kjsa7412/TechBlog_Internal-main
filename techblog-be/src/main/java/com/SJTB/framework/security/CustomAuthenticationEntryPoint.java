package com.SJTB.framework.security;

import com.SJTB.framework.config.FrameConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증 실패 시 처리를 담당하는 클래스입니다.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    /**
     * 인증 실패 시 결과를 처리합니다.
     *
     * @param request   HTTP 요청 객체
     * @param response  HTTP 응답 객체
     * @param ex        발생한 인증 예외
     * @throws IOException 입출력 예외가 발생할 경우
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            // 예외 메시지를 JSON 형태로 반환
            Map<String, String> errorResponse = new HashMap<>();
            String exceptionMessage = (String) request.getAttribute("exception");

            if (exceptionMessage == null) {
                exceptionMessage = "인증에 실패하였습니다.";
            }

            // 토큰 만료 케이스를 제외하고, Access, Refresh 토큰 제거
            if (!exceptionMessage.equals(FrameConstants.JWT_EXPIRED_TOKEN_MSG)) {
                // 기존에 있던 토큰과 이름은 동일하지만 value는 빈칸, maxAge는 0인 쿠키를 생성
                // 새로 생성된 쿠키는 브라우저에 있는 기존의 쿠키를 덮어쓴 후 사라짐
                ResponseCookie accessCookie = ResponseCookie.from(FrameConstants.JWT_ACCESS_TOKEN_NAME, "")
                        .httpOnly(true)
                        .path("/")
                        .maxAge(0)
                        .build();

                ResponseCookie refreshCookie = ResponseCookie.from(FrameConstants.JWT_REFRESH_TOKEN_NAME, "")
                        .httpOnly(true)
                        .path("/")
                        .maxAge(0)
                        .build();

                // response 헤더에 쿠키값 할당
                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            }

            logger.error("Authentication Exception : " + exceptionMessage);
            errorResponse.put("errorMsg", exceptionMessage);

            // JSON으로 변환하여 응답
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            logger.error("[commence] 예외 발생: {}", e.getMessage());
            throw e;
        }
    }
}
