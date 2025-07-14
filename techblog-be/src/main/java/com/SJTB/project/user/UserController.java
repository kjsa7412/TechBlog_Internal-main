package com.SJTB.project.user;

import com.SJTB.framework.data.ResultVo;
import com.SJTB.project.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;


    @GetMapping("/public/get/user/info")
    public ResultVo<UserResponseDto> getUserInfo(@RequestParam String userId){
        return userService.getUserInfoByUserId(userId);
    }

    /**
     * 사용자 회원가입 요청을 처리합니다.
     *
     * @param userRequestDto 회원가입할 사용자 정보
     * @return 회원가입 성공 시 IsError가 false인 ResultVo를 반환하고, 실패 시 IsError가 true인 정보를 응답합니다.
     */
    @PostMapping("/public/post/user/signUp")
    public ResultVo<UserResponseDto> UserSignUp(HttpServletRequest request, @RequestBody UserRequestDto userRequestDto) {
        return userService.UserSignUp(request, userRequestDto);
    }

    /**
     * 사용자 프로필 수정을 처리합니다.
     *
     * @param userRequestDto 수정할 사용자 정보
     * @return 회원수정 성공 시 IsError가 false인 ResultVo를 반환하고, 실패 시 IsError가 true인 정보를 응답합니다.
     */
    @PostMapping("/private/post/user/signEdit")
    public ResultVo<UserResponseDto> UserSignEdit(HttpServletRequest request,
                                                  @RequestPart(value = "param") UserRequestDto userRequestDto,
                                                  @RequestPart(value = "image", required = false) MultipartFile image) {

        return userService.UserSignEdit(request, userRequestDto, image);
    }
}
