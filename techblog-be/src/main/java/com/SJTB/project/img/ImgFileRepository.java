package com.SJTB.project.img;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgFileRepository extends JpaRepository<ImgFileEntity, Integer>, ImgFileRepositoryCustom{
    // 이미지 ID를 이용하여 useYN 변경
    @Modifying
    @Query("UPDATE ImgFileEntity i " +
            "SET i.useyn = :useYn, i.finaRegId = :userId, i.finaRegIp = :userIp, i.finaRegDt = CURRENT_TIMESTAMP " +
            "WHERE i.imgid = :imgId")
    void updateImageUseYN(@Param("imgId") Integer imgId, @Param("useYn") String useYn, @Param("userId") String userId, @Param("userIp") String userIp);
}
