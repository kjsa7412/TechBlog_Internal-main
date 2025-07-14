package com.SJTB.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 권한이 없는 예외가 발생했을 경우 처리하는 클래스입니다.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    /**
     * 권한이 없는 경우에 대한 처리를 수행합니다.
     *
     * @param request   HTTP 요청 객체
     * @param response  HTTP 응답 객체
     * @param exception 발생한 접근 거부 예외
     * @throws IOException 입출력 예외가 발생할 경우
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            // 예외 메시지를 JSON 형태로 반환
            Map<String, String> errorResponse = new HashMap<>();
            String exceptionMessage = "NOT-ALLOW";

            logger.error("AccessDenied : " + exceptionMessage);
            errorResponse.put("errorMsg", exceptionMessage);

            // JSON으로 변환하여 응답
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            logger.error("[handle] 예외 발생: {}", e.getMessage());
            throw e;
        }
    }
}
