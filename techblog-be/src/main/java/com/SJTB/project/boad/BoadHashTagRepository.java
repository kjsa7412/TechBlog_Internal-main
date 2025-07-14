package com.SJTB.project.boad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoadHashTagRepository extends JpaRepository<BoadHashTagEntity, Integer>, BoadHashTagRepositoryCustom {

}
