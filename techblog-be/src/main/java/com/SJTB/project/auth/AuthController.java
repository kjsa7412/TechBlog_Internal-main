package com.SJTB.project.auth;

import com.SJTB.framework.data.ResultVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    /**
     * 사용자 로그인 요청을 처리합니다.
     *
     * @param authRequestDto 로그인 데이터를 포함한 요청 본문.
     * @return 사용자 정보와 액세스, 리프레시 토큰이 포함된 ResultVo를 성공 시 반환하고, 실패 시 잘못된 요청으로 응답합니다.
     */
    @PostMapping(value = "public/post/auth/signIn")
    public ResultVo<AuthResponseDto> authSignIn(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthRequestDto authRequestDto) {
        return authService.authSignIn(request, response, authRequestDto);
    }

    /**
     * 사용자 로그아웃 요청을 처리합니다.
     *
     * @return 성공 시 빈 ResultVO를 반환하고, 실패 시 잘못된 요청으로 응답합니다.
     */
    @PostMapping(value = "private/post/auth/signOut")
    public ResultVo<AuthResponseDto> authSignOut(HttpServletRequest request, HttpServletResponse response) {
        return authService.authSignOut(request, response);
    }

    /**
     * 사용자 AccessToken 갱신 요청을 처리합니다.
     *
     * @return 사용자 정보와 액세스, 리프레시 토큰이 포함된 ResultVo를 성공 시 반환하고, 실패 시 잘못된 요청으로 응답합니다.
     */
    @PostMapping(value = "public/post/auth/renewToken")
    public ResultVo<AuthResponseDto> authRenewToken(HttpServletRequest request, HttpServletResponse response) {
        return authService.authRenewToken(request, response);
    }
}
