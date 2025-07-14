package com.SJTB.project.md;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MdFileRepository extends JpaRepository<MdFileEntity, Integer>, MdFileRepositoryCustom {

}
