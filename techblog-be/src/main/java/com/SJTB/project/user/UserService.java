package com.SJTB.project.user;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.data.ResultVo;
import com.SJTB.framework.security.RC4PasswordEncoder;
import com.SJTB.framework.security.SecurityUserUtil;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.framework.utils.FrameStringUtil;
import com.SJTB.project.base.BaseService;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogRepository;
import com.SJTB.project.img.ImgFileEntity;
import com.SJTB.project.img.ImgFileRepository;
import com.SJTB.project.img.ImgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class UserService extends BaseService {
    private final UserRepository userRepository;
    private final RC4PasswordEncoder rc4PasswordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ImgService imgService;
    private final ImgFileRepository imgFileRepository;

    @Autowired
    public UserService(ClntLogRepository clntLogRepository, SvrLogRepository svrLogRepository, UserRepository userRepository, RC4PasswordEncoder rc4PasswordEncoder,
                       ImgService imgService, ImgFileRepository imgFileRepository) {
        super(clntLogRepository, svrLogRepository);
        this.userRepository = userRepository;
        this.rc4PasswordEncoder = rc4PasswordEncoder;
        this.imgService = imgService;
        this.imgFileRepository = imgFileRepository;
    }

    public ResultVo<UserResponseDto> getUserInfoByUserId(String userId) {
        ResultVo<UserResponseDto> resultVO = new ResultVo<>();
        UserEntity userEntity = userRepository.findById(userId);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                                            .userId(userEntity.getUserId())
                                            .userName(userEntity.getUserName())
                                            .profilePicPath(userEntity.getProfilePic() != null ? FrameStringUtil.setThumbnailUrlPath(userEntity.getProfilePic()) : null) // null 체크 추가
                                            .profileCont(userEntity.getProfileCont())
                                            .build();
        resultVO.setContent(userResponseDto);
        return resultVO;
    }

    @Transactional
    public ResultVo<UserResponseDto> UserSignUp(HttpServletRequest request, UserRequestDto userRequestDto) {
        ResultVo<UserResponseDto> resultVO = new ResultVo<>(UserResponseDto.class);
        String userIP = FrameHttpUtil.getUserIp(request);
        LocalDateTime nowTime = FrameDateUtil.getLocalDateTime();
        String userID = userRequestDto.getUserId();
        try {
            // 이메일 중복 확인
            if (userRepository.existsByUserId(userRequestDto.getUserId())) {
                logger.error("***** 이미 사용중인 이메일 입니다 ******");
                resultVO.setIsError(true);
                resultVO.setErrorMsg("***** 이미 사용중인 이메일 입니다 ******");
            } else {
                // UserEntity 객체 생성
                UserEntity userEntity = UserEntity.builder()
                        .userId(userRequestDto.getUserId())
                        .passWd(rc4PasswordEncoder.encode(userRequestDto.getUserPw()))
                        .userName(userRequestDto.getUserName())
                        .useYN("Y")
                        .userAuth("2") // ("1" : 관리자, "2": 사용자)
                        .firsRegId("SYSTEM")
                        .firsRegIp(userIP)
                        .firsRegDt(nowTime)
                        .build();

                // 사용자 저장
                userRepository.save(userEntity);

                // 저장에 성공한 경우 결과 설정
                UserResponseDto userResponseDto = UserResponseDto.builder()
                        .userId(userEntity.getUserId())
                        .build();

                resultVO.setContent(userResponseDto);

                userID = userEntity.getUserId();
            }
        } catch (Exception e) {
            logger.error("***** 회원가입 실패 ***** : " + e.getMessage(), e);
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 회원가입 실패 ***** : " + e.getMessage());
        } finally{
            saveClientLog(userID, FrameConstants.BLOG_SIGNUP,1,FrameHttpUtil.clientBroswserInfo(request));
        }

        return resultVO;
    }

    @Transactional
    public ResultVo<UserResponseDto> UserSignEdit(HttpServletRequest request, UserRequestDto userRequestDto, MultipartFile image) {
        ResultVo<UserResponseDto> resultVO = new ResultVo<>(UserResponseDto.class);
        String userId = SecurityUserUtil.getCurrentUser();
        String userIP = FrameHttpUtil.getUserIp(request);
        LocalDateTime nowTime = FrameDateUtil.getLocalDateTime();

        // 프로필 사진 변수
        Integer profilePic = userRequestDto.getProfilePic();

        try {
            ImgFileEntity imgFileEntity;

            // 프로필 사진 등록 및 수정 요청
            if (image != null && !image.isEmpty()) {
                imgFileEntity = imgService.insertProfileImage(request, image, userId);

                // 이미지 저장에 실패한 경우
                if (imgFileEntity.getImgid() == null) {
                    logger.error("***** 회원 정보수정 실패 ***** : 프로필 사진 저장 실패");
                    resultVO.setIsError(true);
                    resultVO.setErrorMsg("***** 회원 정보수정 실패 ***** : 프로필 사진 저장 실패");
                    return resultVO;
                } else {
                    // 기존 사진 useYN 변경
                    imgFileRepository.updateImageUseYN(userRequestDto.getProfilePic(), "N", userId, userIP);

                    // 새로운 프로필 사진 변수 할당
                    profilePic = imgFileEntity.getImgid();
                }
            }

            // 프로필 사진 삭제 요청
            if (userRequestDto.getDelState().equals("del")) {
                // 기존 사진 useYN 변경
                imgFileRepository.updateImageUseYN(userRequestDto.getProfilePic(), "N", userId, userIP);

                // 프로필 제거 변수 할당
                profilePic = null;
            }

            // UserEntity 객체 생성
            UserEntity userEntity = UserEntity.builder()
                    .userId(userId)
//                    .passWd(rc4PasswordEncoder.encode(userRequestDto.getUserPw()))
                    .userName(userRequestDto.getUserName())
                    .profileCont(userRequestDto.getProfileCont())
                    .profilePic(profilePic == null ? null : imgFileRepository.getById(profilePic))
                    .finaRegId(userId)
                    .finaRegIp(userIP)
                    .finaRegDt(nowTime)
                    .build();

            // 사용자 저장
            userEntity = userRepository.updateUserInfo(request, userEntity);

            // 저장에 성공한 경우 결과 설정
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .userId(userEntity.getUserId())
                    .userName(userEntity.getUserName())
                    .profileCont(userEntity.getProfileCont())
                    .profilePic(userEntity.getProfilePic() != null ? userEntity.getProfilePic().getImgid() : null) // null 체크 추가
                    .profilePicPath(userEntity.getProfilePic() != null ? FrameStringUtil.setThumbnailUrlPath(userEntity.getProfilePic()) : null) // null 체크 추가
                    .userAuth(userEntity.getUserAuth())
                    .build();

            resultVO.setContent(userResponseDto);

        } catch (Exception e) {
            logger.error("***** 회원 정보수정 실패 ***** : " + e.getMessage(), e);
            resultVO.setIsError(true);
            resultVO.setErrorMsg("***** 회원 정보수정 실패 ***** : " + e.getMessage());
        } finally{
            saveClientLog(userId, FrameConstants.BLOG_PROFILE_UPDATE,0, FrameHttpUtil.clientBroswserInfo(request));
        }

        return resultVO;
    }
}
