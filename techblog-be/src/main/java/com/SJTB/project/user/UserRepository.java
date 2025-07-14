package com.SJTB.project.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// 특정 열만 Select 할떄 필요한 매핑
interface UserInfoMapping {
    String getUserId();
    String getUserAuth();
}

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>, UserRepositoryCustom{
    @Query("SELECT u.userId, u.userAuth " +
            "FROM UserEntity u")
    List<UserInfoMapping> findAllByJPQL();

    // ID 중복을 검사하는 메소드
    boolean existsByUserId(String userId);
}