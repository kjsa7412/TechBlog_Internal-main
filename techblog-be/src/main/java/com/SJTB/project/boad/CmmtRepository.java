package com.SJTB.project.boad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmmtRepository extends JpaRepository<CmmtEntity, Integer>, CmmtRepositoryCustom {

    @Query("SELECT a FROM CmmtEntity a JOIN FETCH a.user u WHERE a.boadid = :boadid and a.useyn = 'Y' ORDER BY a.cmtid DESC ")
    List<CmmtEntity> findCmmtListByBoadId(@Param("boadid") Integer boadid);

    @Query("SELECT COUNT(a) FROM CmmtEntity a JOIN a.user u WHERE a.boadid = :boadid and a.useyn = 'Y' ")
    long findCmmtListByBoadIdCount(@Param("boadid") Integer boadid);
}
