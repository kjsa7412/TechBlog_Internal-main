package com.SJTB.project.boad;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;


import java.util.List;

public interface BoadContRepositoryCustom {
    List<BoadContEntity> findAllBoadContByBoadJoin(int boadid);
    long deleteByBoadId(int boadid);
}

@Repository
@RequiredArgsConstructor
class BoadContRepositoryCustomImpl implements BoadContRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final com.SJTB.project.boad.QBoadContEntity qBoadContEntity = com.SJTB.project.boad.QBoadContEntity.boadContEntity;

    public List<BoadContEntity> findAllBoadContByBoadJoin(int boadid){
        List<BoadContEntity> result = queryFactory.selectFrom(qBoadContEntity)
                .where(qBoadContEntity.boadid.eq(boadid))
                .orderBy(qBoadContEntity.contid.asc())
                .fetch();
        return result;
    }

    public long deleteByBoadId(int boadid){
        long result = queryFactory.delete(qBoadContEntity)
                        .where(qBoadContEntity.boadid.eq(boadid))
                        .execute();
        return result;
    }

}