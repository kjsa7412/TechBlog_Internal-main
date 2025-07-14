package com.SJTB.project.user;

import java.util.List;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.security.RC4PasswordEncoder;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.project.boad.BoadRequestDto;
import com.SJTB.project.user.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;

public interface UserRepositoryCustom {
    List<UserEntity> findAllUsersQueryDSL();
    UserEntity findById(String id);
    UserEntity updateUserInfo(HttpServletRequest request, UserEntity req);
}

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final RC4PasswordEncoder rc4PasswordEncoder;

    public List<UserEntity> findAllUsersQueryDSL() {
        return queryFactory
                .select(Projections.fields(UserEntity.class,
                        qUserEntity.userId))
                .from(qUserEntity)
                .fetch();
    }

    public UserEntity findById(String id) {

        QUserEntity qUserEntity = QUserEntity.userEntity;

        UserEntity user = queryFactory.selectFrom(qUserEntity)
                        .where(qUserEntity.userId.eq(id))
                        .fetchOne();

        return user;
    }

    public UserEntity updateUserInfo(HttpServletRequest request, UserEntity req){
//        BooleanExpression whereCondition = qUserEntity.userId.eq(req.getUserId())
//                                            .and(qUserEntity.passWd.eq(req.getPassWd()));
        BooleanExpression whereCondition = qUserEntity.userId.eq(req.getUserId());

        long updateRowCnt = queryFactory.update(qUserEntity)
                                .where(whereCondition)
                                .set(qUserEntity.userName, req.getUserName())
//                                .set(qUserEntity.passWd, rc4PasswordEncoder.encode(req.getPassWd()))
//                                .set(qUserEntity.passWd, req.getPassWd())
                                .set(qUserEntity.profilePic, req.getProfilePic())
                                .set(qUserEntity.profileCont, req.getProfileCont())
                                .set(qUserEntity.finaRegDt, FrameDateUtil.getLocalDateTime())
                                .set(qUserEntity.finaRegId, req.getUserId())
                                .set(qUserEntity.finaRegIp, FrameHttpUtil.getUserIp(request))
                                .execute();
        if(updateRowCnt > 0){
            UserEntity userEntity = queryFactory.selectFrom(qUserEntity)
                                    .where(whereCondition)
                                    .fetchOne();
            return userEntity;
        } else {
            return UserEntity.builder().build();
        }
    }
}