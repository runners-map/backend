package com.service.runnersmap.service;

import com.service.runnersmap.entity.Rank;
import com.service.runnersmap.repository.RankRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

  private final RankRepository rankRepository;


  /*
   * 랭킹 조회
   */
  public List<Rank> searchRankByMonth(Integer year, Integer month
  ) throws Exception {
    return rankRepository.findAllByYearAndMonth(year, month);
  }


}
