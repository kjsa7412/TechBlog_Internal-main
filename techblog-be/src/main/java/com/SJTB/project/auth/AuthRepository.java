package com.SJTB.project.auth;

import com.SJTB.project.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<UserEntity, String> {
    // 사용자 ID를 사용하여 사용자 조회
    UserEntity findByUserId(String userId);

    // 사용자 ID를 사용하여 리프레시 토큰과 마지막 로그인 일시 수정
    @Modifying
    @Query("UPDATE UserEntity u " +
            "SET u.loginToken = :loginToken, u.lastLoginDt = CURRENT_TIMESTAMP, " +
            "u.finaRegId = :userId, u.finaRegIp = :userIp, u.finaRegDt = CURRENT_TIMESTAMP " +
            "WHERE u.userId = :userId")
    void updateTokenAndLastDt(@Param("loginToken") String loginToken, @Param("userId") String userId, @Param("userIp") String userIp);

    // 사용자 ID를 사용하여 리프레시 토큰 삭제
    @Modifying
    @Query("UPDATE UserEntity u " +
            "SET u.loginToken = '', u.finaRegId = :userId, u.finaRegIp = :userIp, u.finaRegDt = CURRENT_TIMESTAMP " +
            "WHERE u.userId = :userId")
    void deleteToken(@Param("userId") String userId, @Param("userIp") String userIp);

    // 사용자 IO와 토큰을 사용하여 사용자 조회
    UserEntity findByUserIdAndLoginToken(String userId, String token);
}