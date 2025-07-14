package com.SJTB.project.boad;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BoadHashTagRepositoryCustom {
    List<BoadHashTagEntity> fineAllHashTagByBoadid(int boadid);
    long deleteHashTag(int boadid);

}

@Repository
@RequiredArgsConstructor
class BoadHashTagRepositoryCustomImpl implements BoadHashTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final com.SJTB.project.boad.QBoadHashTagEntity qBoadHashTagEntity = com.SJTB.project.boad.QBoadHashTagEntity.boadHashTagEntity;

    public List<BoadHashTagEntity> fineAllHashTagByBoadid(int boadid){
        List<BoadHashTagEntity> result = queryFactory.selectFrom(qBoadHashTagEntity)
                .where(qBoadHashTagEntity.boad.boadId.eq(boadid))
                .orderBy(qBoadHashTagEntity.hashid.asc())
                .fetch();
        return result;
    }

    public long deleteHashTag(int boadid){
        long deleteRowCnt = queryFactory.delete(qBoadHashTagEntity)
                    .where(qBoadHashTagEntity.boad.boadId.eq(boadid))
                    .execute();
        return deleteRowCnt;
    }
}