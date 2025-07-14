package com.SJTB.project.auth;


import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.data.ResultVo;
import com.SJTB.framework.security.JwtTokenProvider;
import com.SJTB.framework.security.RC4PasswordEncoder;
import com.SJTB.framework.security.SecurityUserUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.framework.utils.FrameStringUtil;
import com.SJTB.project.base.BaseService;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogRepository;
import com.SJTB.project.user.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(ClntLogRepository clntLogRepository, SvrLogRepository svrLogRepository, AuthRepository authRepository, JwtTokenProvider jwtTokenProvider) {
        super(clntLogRepository, svrLogRepository);
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param authRequestDto 로그인에 필요한 데이터를 포함하는 Dto
     * @return 로그인된 사용자 정보 authResponseDto
     */
    @Transactional
    public ResultVo<AuthResponseDto> authSignIn(HttpServletRequest request, HttpServletResponse response, AuthRequestDto authRequestDto) {
        ResultVo<AuthResponseDto> resultVO = new ResultVo<>();
        String userIP = FrameHttpUtil.getUserIp(request);

        // 사용자 정보 검색
        UserEntity userInfo = authRepository.findByUserId(authRequestDto.getUserId());

        if (userInfo == null) {
            logger.error("***** 등록된 사용자가 존재하지 않습니다. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 등록된 사용자가 존재하지 않습니다. ******");
        }
        else if (userInfo.getUseYN().equals("N")) {
            logger.error("***** 사용이 중지된 계정입니다. 관리자에게 문의하세요. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 사용이 중지된 계정입니다. 관리자에게 문의하세요. ******");
        } else {
            RC4PasswordEncoder passwordEncoder = new RC4PasswordEncoder();

            if (passwordEncoder.matches(String.valueOf(authRequestDto.getUserPw()), userInfo.getPassWd())) {
                String accessToken = jwtTokenProvider.createAccessToken(userInfo.getUserId(), userInfo.getUserAuth());
                String refreshToken = jwtTokenProvider.createRefreshToken(userInfo.getUserId(), userInfo.getUserAuth());

                // 마지막 로그인 일시와 리프레시 토큰 update
                authRepository.updateTokenAndLastDt(refreshToken, userInfo.getUserId(), userIP);

                // 인증에 성공한 경우 결과 설정
                // 사용자 프로필은 DTO로 전달, 토큰은 쿠키로 설정
                AuthResponseDto authResponseDto = AuthResponseDto.builder()
                        .userId(userInfo.getUserId())
                        .userName(userInfo.getUserName())
                        .profileCont(userInfo.getProfileCont())
                        .profilePic(userInfo.getProfilePic() != null ? userInfo.getProfilePic().getImgid() : null) // null 체크 추가
                        .profilePicPath(userInfo.getProfilePic() != null ? FrameStringUtil.setThumbnailUrlPath(userInfo.getProfilePic()) : null) // null 체크 추가
                        .userAuth(userInfo.getUserAuth())
                        .build();

                resultVO.setContent(authResponseDto);

                // accessToken 할당
                ResponseCookie accessCookie = ResponseCookie.from(FrameConstants.JWT_ACCESS_TOKEN_NAME, accessToken)
                        .httpOnly(true)
//                        .secure(true) todo: HTTPS 통신 시 사용
//                        .sameSite("None")
                        .path("/")
                        .build();

                // refreshToken 할당
                ResponseCookie refreshCookie = ResponseCookie.from(FrameConstants.JWT_REFRESH_TOKEN_NAME, refreshToken)
                        .httpOnly(true)
//                        .secure(true) todo: HTTPS 통신 시 사용
//                        .sameSite("None")
                        .path("/")
                        .build();

                // response 헤더에 쿠키값 할당
                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

                saveClientLog(authRequestDto.getUserId(), FrameConstants.BLOG_LOGIN,1, FrameHttpUtil.clientBroswserInfo(request));
            } else {
                logger.error("***** 비밀번호가 일치하지 않습니다. ******");
                resultVO.setIsError(true);
                resultVO.setErrorMsg("***** 비밀번호가 일치하지 않습니다. ******");
            }
        }
        return resultVO;
    }

    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @return 성공시 비어있는 authResponseDto
     */
    @Transactional
    public ResultVo<AuthResponseDto> authSignOut(HttpServletRequest request, HttpServletResponse response) {
        ResultVo<AuthResponseDto> resultVO = new ResultVo<>();
        String userId = SecurityUserUtil.getCurrentUser();
        String userIP = FrameHttpUtil.getUserIp(request);

        // RefreshToken 토큰 제거
        authRepository.deleteToken(userId, userIP);

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

        saveClientLog(userId, FrameConstants.BLOG_LOGOUT,1, FrameHttpUtil.clientBroswserInfo(request));

        return resultVO;
    }

    /**
     * RefreshToken을 사용하여 사용자 AccessToken을 갱신합니다.
     *
     * @return 로그인된 사용자 정보 authResponseDto
     */
    @Transactional
    public ResultVo<AuthResponseDto> authRenewToken(HttpServletRequest request, HttpServletResponse response) {
        ResultVo<AuthResponseDto> resultVO = new ResultVo<>();
        String userIP = FrameHttpUtil.getUserIp(request);
        boolean renewAllow = false;

        // AccessToken 정보 추출
        String userAccessToken = jwtTokenProvider.resolveAccessToken(request);
        String accessTokenUserId = jwtTokenProvider.getUserIdFromAccessToken(userAccessToken);

        // RefreshToken 정보 추출
        String userRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String refreshTokenUserId = jwtTokenProvider.getUserIdFromRefreshToken(userRefreshToken);

        // 사용자 정보 검색
        UserEntity userInfo = authRepository.findByUserIdAndLoginToken(refreshTokenUserId, userRefreshToken);

        // 사용자가 없는 경우 재발급 허가 X
        if (userInfo == null) {
            logger.error("***** 등록된 사용자가 존재하지 않습니다. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 등록된 사용자가 존재하지 않습니다. ******");
        }
        // 사용이 중지된 경우 재발급 허가 X
        else if (userInfo.getUseYN().equals("N")) {
            logger.error("***** 사용이 중지된 계정입니다. 관리자에게 문의하세요. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 사용이 중지된 계정입니다. 관리자에게 문의하세요. ******");
        }
        // AccessToken이 없는 경우 재발급 허가 X
        else if (FrameStringUtil.isEmpty(userAccessToken)) {
            logger.error("***** AccessToken이 존재하지 않습니다. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 로그인 정보가 존재하지 않습니다. ******");
        }
        // AccessToken의 이메일, RefreshToken의 이메일이 모두 동일한지 확인
        else if (!accessTokenUserId.equals(refreshTokenUserId)) {
            logger.error("***** 토큰 재발급 조건이 올바르지 않습니다. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 로그인 정보가 올바르지 않습니다. ******");
        }
        // RefreshToken의 유효성 검사
        else if (!jwtTokenProvider.validateRefreshToken(userRefreshToken)) {
            logger.error("***** 올바르지 못한 RefreshToken 입니다. ******");
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 로그인 정보가 존재하지 않습니다. ******");
        }
        // 토큰 재발급 권한이 유효한 경우
        else {
            renewAllow = true;
        }

        // 재발급이 허용된 경우에만 재발급, 허용되지 않은 경우 DB에 있는 토큰과 사용자 인증 정보를 초기화
        if (renewAllow) {
            String accessToken = jwtTokenProvider.createAccessToken(userInfo.getUserId(), userInfo.getUserAuth());
            String refreshToken = jwtTokenProvider.createRefreshToken(userInfo.getUserId(), userInfo.getUserAuth());

            // 마지막 로그인 일시와 리프레시 토큰 update
            authRepository.updateTokenAndLastDt(refreshToken, userInfo.getUserId(), userIP);

            // 인증에 성공한 경우 결과 설정
            // 사용자 프로필은 DTO로 전달, 토큰은 쿠키로 설정
            AuthResponseDto authResponseDto = AuthResponseDto.builder()
                    .userId(userInfo.getUserId())
                    .userName(userInfo.getUserName())
                    .profileCont(userInfo.getProfileCont())
                    .profilePic(userInfo.getProfilePic() != null ? userInfo.getProfilePic().getImgid() : null) // null 체크 추가
                    .profilePicPath(userInfo.getProfilePic() != null ? FrameStringUtil.setThumbnailUrlPath(userInfo.getProfilePic()) : null) // null 체크 추가
                    .userAuth(userInfo.getUserAuth())
                    .build();

            resultVO.setContent(authResponseDto);

            // accessToken 할당
            ResponseCookie accessCookie = ResponseCookie.from(FrameConstants.JWT_ACCESS_TOKEN_NAME, accessToken)
                    .httpOnly(true)
//                        .secure(true) todo: HTTPS 통신 시 사용
//                        .sameSite("None")
                    .path("/")
                    .build();

            // refreshToken 할당
            ResponseCookie refreshCookie = ResponseCookie.from(FrameConstants.JWT_REFRESH_TOKEN_NAME, refreshToken)
                    .httpOnly(true)
//                        .secure(true) todo: HTTPS 통신 시 사용
//                        .sameSite("None")
                    .path("/")
                    .build();

            // response 헤더에 쿠키값 할당
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        } else {
            // RefreshToken 토큰 제거 (userId을 포함하는 2가지 경우의 수 모두 제거)
            authRepository.deleteToken(accessTokenUserId, userIP);
            authRepository.deleteToken(refreshTokenUserId, userIP);

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
        saveClientLog(FrameStringUtil.isNullDefaultValue(refreshTokenUserId, "anonymous"), FrameConstants.BLOG_RENEW,1, FrameHttpUtil.clientBroswserInfo(request));

        return resultVO;
    }
}
