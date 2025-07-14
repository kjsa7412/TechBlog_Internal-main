package com.SJTB.project.boad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoadContRepository extends JpaRepository<BoadContEntity, Integer>, BoadContRepositoryCustom {

}