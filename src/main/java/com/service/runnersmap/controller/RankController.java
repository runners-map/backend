package com.service.runnersmap.controller;

import com.service.runnersmap.dto.RankDto;
import com.service.runnersmap.entity.Rank;
import com.service.runnersmap.service.RankService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankController {

  private final RankService rankService;

  /*
   * 랭킹 조회
   * - 월별로 집계된 데이터를 순위별로 조회한다.
   */
  @GetMapping
  public ResponseEntity<List<RankDto>> searchRankByMonth(
      @RequestParam(value = "year") Integer year,
      @RequestParam(value = "month") Integer month
  ) throws Exception {
    List<Rank> rank = rankService.searchRankByMonth(year, month);

    if (rank.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(
          rank.stream().map(p -> RankDto.fromEntity(p)).collect(Collectors.toList())

      );
    }
  }

}
