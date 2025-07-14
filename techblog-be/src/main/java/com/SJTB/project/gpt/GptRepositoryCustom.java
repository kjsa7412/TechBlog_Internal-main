package com.SJTB.project.gpt;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;



public interface GptRepositoryCustom {

}

@Repository
@RequiredArgsConstructor
class GptRepositoryImpl implements GptRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}