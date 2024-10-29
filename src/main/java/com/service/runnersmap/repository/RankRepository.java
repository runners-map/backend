package com.service.runnersmap.repository;

import com.service.runnersmap.entity.Rank;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

  List<Rank> findAllByRankPositionBetweenAndYearAndMonthOrderByRankPosition(Integer startRankPosition, Integer endRankPosition, Integer year, Integer month);


  @Transactional
  @Modifying
  void deleteByYearAndMonth(Integer year, Integer month);
}
