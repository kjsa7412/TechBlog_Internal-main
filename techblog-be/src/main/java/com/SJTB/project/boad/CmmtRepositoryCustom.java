package com.SJTB.project.boad;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CmmtRepositoryCustom {
//    List<CmmtEntity> findCmtListByBoadId(Integer boadid);
}

@Repository
@RequiredArgsConstructor
class CmmtRepositoryCustomImpl implements CmmtRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final com.SJTB.project.boad.QCmmtEntity qCmmtEntity = com.SJTB.project.boad.QCmmtEntity.cmmtEntity;

    public List<CmmtEntity> findCmtListByBoadId(Integer boadid){
        List<CmmtEntity> result = queryFactory.selectFrom(qCmmtEntity)
                .where(qCmmtEntity.boadid.eq(boadid))
                .orderBy(qCmmtEntity.cmtid.asc())
                .fetch();
        return result;
    }
}
