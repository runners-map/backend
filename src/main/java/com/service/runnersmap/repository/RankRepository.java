package com.service.runnersmap.repository;

import com.service.runnersmap.entity.Rank;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {

  @Query("SELECT r FROM Rank r WHERE r.year = :year AND r.month = :month ORDER BY r.rankPosition ASC")
  List<Rank> findAllByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

}
