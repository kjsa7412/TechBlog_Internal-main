package com.SJTB.project.md;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

public interface MdFileRepositoryCustom {
    long selectByCountByBoadId(int boadid);
}

@Repository
@RequiredArgsConstructor
class MdFileRepositoryCustomImpl implements MdFileRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final com.SJTB.project.md.QMdFileEntity qMdFileEntity = com.SJTB.project.md.QMdFileEntity.mdFileEntity;

    public long selectByCountByBoadId(int boadid){
        long selectRowCnt = queryFactory.select(qMdFileEntity.count())
                            .from(qMdFileEntity)
                            .where(qMdFileEntity.boadid.eq(boadid))
                            .fetchOne();
        return selectRowCnt;
    }
}
