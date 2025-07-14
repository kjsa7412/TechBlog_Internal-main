package com.SJTB.project.base;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


public interface SvrLogRepositoryCustom {

}

@Repository
@RequiredArgsConstructor
class SvrLogRepositoryImpl implements SvrLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}