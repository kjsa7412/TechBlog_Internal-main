package com.SJTB.project.base;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


public interface ClntLogRepositoryCustom {

}

@Repository
@RequiredArgsConstructor
class ClntLogRepositoryImpl implements ClntLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}