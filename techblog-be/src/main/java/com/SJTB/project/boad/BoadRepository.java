package com.SJTB.project.boad;

import com.SJTB.project.img.ImgFileEntity;
import com.SJTB.project.user.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface BoadRepository extends JpaRepository<BoadEntity, Integer>, BoadRepositoryCustom {
    @Query("SELECT b " +
            "FROM BoadEntity b join fetch b.user")
    // 이렇게도 사용 가능 @Query("SELECT b FROM BoadEntity b JOIN b.user e")
    List<BoadEntity> findAllByFetchJoin();

    //게시글 검색 목록
//    @Query(value = "SELECT b FROM BoadEntity b JOIN FETCH b.user JOIN FETCH b.hashtag c WHERE b.title LIKE %:searchWord% OR c.hashtag LIKE %:searchWord%")
    @Query(value = "SELECT b FROM BoadEntity b JOIN FETCH b.user WHERE b.title LIKE %:searchWord%")
    List<BoadEntity> findAllByFetchJoin(@Param("searchWord") String searchWord);

    // 게시물 수정 메소드
    @Modifying
    @Query("UPDATE BoadEntity b " +
            "SET b.title = :title, b.summary = :summary, b.thumbnail = :thumbnail, b.lastMd = :lastMd, " +
            "b.finaRegId = :userId, b.finaRegIp = :userIp, b.finaRegDt = CURRENT_TIMESTAMP " +
            "WHERE b.boadId = :boadId AND b.user = :user")
    void updateBoadCont(@Param("title") String title, @Param("summary") String summary, @Param("thumbnail") ImgFileEntity thumbnail,
                        @Param("lastMd") Integer lastMd, @Param("userId") String userId, @Param("userIp") String userIp,
                        @Param("user") UserEntity user, @Param("boadId") Integer boadId);

    // 게시물 삭제 메소드
    @Modifying
    @Query("UPDATE BoadEntity b " +
            "SET b.useYN = 'N', " +
            "b.finaRegId = :userId, b.finaRegIp = :userIp, b.finaRegDt = CURRENT_TIMESTAMP " +
            "WHERE b.boadId = :boadId AND b.user = :user")
    void updateBoadStatus(@Param("userId") String userId, @Param("userIp") String userIp,
                              @Param("user") UserEntity user, @Param("boadId") Integer boadId);
}