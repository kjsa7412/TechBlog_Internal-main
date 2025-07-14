package com.SJTB.project.base;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SvrLogRepository extends JpaRepository<SvrLogEntity, Integer>, SvrLogRepositoryCustom {

}
