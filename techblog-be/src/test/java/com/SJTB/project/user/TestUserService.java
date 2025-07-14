package com.SJTB.project.user;

import com.SJTB.framework.security.RC4PasswordEncoder;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.project.img.ImgFileEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RC4PasswordEncoder rc4PasswordEncoder;


    JPAQueryFactory queryFactory;
    com.SJTB.project.user.QUserEntity qUserEntity = com.SJTB.project.user.QUserEntity.userEntity;


    @Test
    public void 유저회원가입(){
        UserEntity userEntity = UserEntity.builder()
                .userId("aaa@aaa.comaa")
                .passWd(rc4PasswordEncoder.encode("123123"))
                .userName("유저명")
                .useYN("Y")
                .userAuth("2") // ("1" : 관리자, "2": 사용자)
                .firsRegId("SYSTEM")
                .firsRegIp("1.1.1.1")
                .firsRegDt(FrameDateUtil.getLocalDateTime())
                .build();

        // 사용자 저장
        UserEntity userEntity1 = userRepository.save(userEntity);

        // 저장에 성공한 경우 결과 설정
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .userId(userEntity.getUserId())
                .build();

        assertThat(userEntity1.getUserId()).isEqualTo("aaa@aaa.comaa");
    }


    @Test
    public void 유저프로필정보수정(){
        ImgFileEntity imgFileEntity = ImgFileEntity.builder().imgid(1).imgcate("THUM").imgcateid(1).imghashname("asdasdads.jpg").imgname("test.jpg").build();
        UserEntity userEntity = UserEntity.builder()
                .userId("dade1075@naver.com")
                .passWd(rc4PasswordEncoder.encode("123123"))
                .userName("유저명입니다.")
                .profileCont("프로필 내용입니다.")
                .profilePic(imgFileEntity)
                .finaRegId("SYSTEM")
                .finaRegIp("127.0.0.1")
                .firsRegDt(FrameDateUtil.getLocalDateTime())
                .build();

        // 사용자 저장
        BooleanExpression whereCondition = qUserEntity.userId.eq(userEntity.getUserId())
                .and(qUserEntity.passWd.eq(userEntity.getPassWd()));

        long updateRowCnt = queryFactory.update(qUserEntity)
                .where(whereCondition)
                .set(qUserEntity.userName, userEntity.getUserName())
                .set(qUserEntity.passWd, userEntity.getPassWd())
                .set(qUserEntity.profilePic, userEntity.getProfilePic())
                .set(qUserEntity.profileCont, userEntity.getProfileCont())
                .set(qUserEntity.finaRegDt, FrameDateUtil.getLocalDateTime())
                .set(qUserEntity.finaRegId, userEntity.getUserId())
                .set(qUserEntity.finaRegIp, "127.0.0.1")
                .execute();

        assertThat(updateRowCnt).isNotZero();

    }

}
