package com.SJTB.project.boad;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.project.img.ImgFileEntity;
import com.SJTB.project.img.ImgFileRepository;
import com.SJTB.project.user.QUserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

public interface BoadRepositoryCustom {

    List<BoadEntity>  findPopularList();

    List<BoadEntity> findAllBoadQueryDSLByJoin();

    BoadEntity findByBoadId(Integer boadid);

    BoadEntity updateBoadInfo(HttpServletRequest request, BoadRequestDto req);

    long updateBoadStatus(HttpServletRequest request, BoadRequestDto req);

    void updateBoadView(Integer boadId);

    long updateBoadLike(BoadRequestDto req);

    long updateBoadMdInfo(int mdid, int boadid);
}
@Repository
@RequiredArgsConstructor
class BoadRepositoryCustomImpl implements BoadRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final com.SJTB.project.boad.QBoadEntity qBoadEntity = com.SJTB.project.boad.QBoadEntity.boadEntity;
    private final com.SJTB.project.boad.QBoadHashTagEntity qBoadHashTagEntity = com.SJTB.project.boad.QBoadHashTagEntity.boadHashTagEntity;
    private final QUserEntity qUserEntity = QUserEntity.userEntity;
    private final Logger logger = LoggerFactory.getLogger(BoadRepositoryCustom.class);
    private final ImgFileRepository imgFileRepository;

    public List<BoadEntity> findAllBoadQueryDSLByJoin() {
        List<BoadEntity> result = queryFactory
                .selectFrom(qBoadEntity)
                .leftJoin(qBoadEntity.user, qUserEntity)
                .fetch();

        logger.debug("Query result: {}", result);

        return result;
    }

    //인기 게시글 목록
    public List<BoadEntity> findPopularList() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        List<BoadEntity> result = queryFactory
                                .selectFrom(qBoadEntity)
                                .where(
                                        qBoadEntity.useYN.eq("Y"),
                                        qBoadEntity.views.isNotNull(),
                                        qBoadEntity.likes.isNotNull())
//                                .and(qBoadEntity.finaRegDt.goe(thirtyDaysAgo).or(qBoadEntity.firsRegDt.goe(thirtyDaysAgo))))
                                .orderBy(qBoadEntity.views.desc(), qBoadEntity.likes.desc())
                                .limit(3)
                                .fetch();
        return result;
    }

    @Transactional
    public BoadEntity findByBoadId(Integer id) {

        com.SJTB.project.boad.QBoadEntity qBoadEntity = com.SJTB.project.boad.QBoadEntity.boadEntity;

        BoadEntity boad = queryFactory.selectFrom(qBoadEntity)
//                .leftJoin()
                .where(qBoadEntity.boadId.eq(id).and(qBoadEntity.useYN.eq("Y")))
                .fetchOne();

        return boad;
    }

    /*게시글 수정*/
    @Transactional
    public BoadEntity updateBoadInfo(HttpServletRequest request, BoadRequestDto req){

        BooleanExpression whereCondition = qBoadEntity.boadId.eq(req.getBoadid())
                                            .and(qBoadEntity.user.userId.eq(req.getUserid()));
        ImgFileEntity imgFileEntity = imgFileRepository.getById(req.getThumbnailid());
        long updateRowCnt = queryFactory
                            .update(qBoadEntity)
                            .where(whereCondition)
                            .set(qBoadEntity.title, req.getTitle())
                            .set(qBoadEntity.thumbnail, imgFileEntity)
                            .set(qBoadEntity.openStatus, req.getOpenstatus())
                            .set(qBoadEntity.finaRegId, req.getUserid())
                            .set(qBoadEntity.finaRegDt, LocalDateTime.now())
                            .set(qBoadEntity.finaRegIp, FrameHttpUtil.getUserIp(request))
                            .execute();

        if(updateRowCnt > 0){
            BoadEntity boadEntity = queryFactory.selectFrom(qBoadEntity)
                     .join(qBoadEntity.user, qUserEntity)
                     .fetchJoin()
                     .leftJoin(qBoadEntity.hashtag, qBoadHashTagEntity)
                     .fetchJoin()
                     .where(whereCondition).fetchOne();
            return boadEntity;
        } else {
            return BoadEntity.builder().build();
        }

    }

    /*게시글 삭제(update처리)*/
    @Transactional
    public long updateBoadStatus(HttpServletRequest request, BoadRequestDto req){

        BooleanExpression whereCondition = qBoadEntity.boadId.eq(req.getBoadid())
                                           .and(qBoadEntity.user.userId.eq(req.getUserid()));

        long boadRowCnt = queryFactory
                .update(qBoadEntity)
                .where(whereCondition)
                .set(qBoadEntity.useYN,"N")
                .set(qBoadEntity.finaRegDt, LocalDateTime.now())
                .set(qBoadEntity.finaRegId, req.getUserid())
                .set(qBoadEntity.finaRegIp, FrameHttpUtil.getUserIp(request))
                .execute();
        return boadRowCnt;
    }

    /* 게시글 조회수 +1 */
    @Transactional
    public void updateBoadView(Integer boadId) {
        BooleanExpression whereCondition = qBoadEntity.boadId.eq(boadId)
                .and(qBoadEntity.useYN.eq("Y"));

        queryFactory
                .update(qBoadEntity)
                .where(whereCondition)
                .set(qBoadEntity.views, qBoadEntity.views.add(1))
                .execute();
    }

    /*게시글 좋아요*/
    @Transactional
    public long updateBoadLike(BoadRequestDto req){

        BooleanExpression whereCondition = qBoadEntity.boadId.eq(req.getBoadid())
                                            .and(qBoadEntity.useYN.eq("Y"));

        long boadRowCnt = queryFactory
                        .update(qBoadEntity)
                        .where(whereCondition)
                        .set(qBoadEntity.likes, qBoadEntity.likes.add(1))
                        .execute();
        return boadRowCnt;
    }

    /*MD파일 정보 수정*/
    @Transactional
    public long updateBoadMdInfo(int mdid, int boadid){
        BooleanExpression whereCondition = qBoadEntity.boadId.eq(boadid);

        long boadRowCnt = queryFactory
                .update(qBoadEntity)
                .where(whereCondition)
                .set(qBoadEntity.lastMd, mdid)
                .execute();
        return boadRowCnt;
    }
}