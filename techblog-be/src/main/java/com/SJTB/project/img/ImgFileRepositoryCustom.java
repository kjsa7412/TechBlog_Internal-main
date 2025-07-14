package com.SJTB.project.img;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.SJTB.project.img.QImgFileEntity.imgFileEntity;


@Repository
public interface ImgFileRepositoryCustom {
    ImgFileEntity findByImgName(String imgName);
}

@Repository
@RequiredArgsConstructor
class ImgFileRepositoryCustomImpl implements ImgFileRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final com.SJTB.project.img.QImgFileEntity qImgFileEntity = com.SJTB.project.img.QImgFileEntity.imgFileEntity;

    public ImgFileEntity findByImgName(String imgName) {
        BooleanExpression condition = qImgFileEntity.imghashname.like("%" + imgName + "%");

        return queryFactory.select(qImgFileEntity)
                .from(qImgFileEntity)
                .where(condition)
                .limit(1)
                .fetchOne();
    }
}
