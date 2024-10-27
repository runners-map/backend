package com.service.runnersmap.service;

import com.service.runnersmap.dto.RankSaveDto;
import com.service.runnersmap.entity.Rank;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.RankRepository;
import com.service.runnersmap.repository.UserPostRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankService {

  private final UserPostRepository userPostRepository;
  private final RankRepository rankRepository;
  private final UserRepository userRepository;

  /*
   * 랭킹 조회
   * - 상위 20 등까지만 조회
   * - 추가아이디어 : 본인 등수도 조회
   */
  @Transactional(readOnly = true)
  public List<Rank> searchRankByMonth(Integer year, Integer month
  ) throws Exception {
    return rankRepository.findAllByRankPositionBetweenAndYearAndMonthOrderByRankPosition(1, 20,
        year, month);
  }


  /*
   * < 랭킹 집계 > : rankJob 에서 호출
   * - 월별 러닝 기록 데이터를 통해 점수를 계산하여 순위별로 사용자 정보를 저장한다. (러닝 유효 Y, 현재 유효한 사용자)
   * - 점수 계산 방식 (좀 더 고민 필요)
   * 1) 거리와 시간을 모두 고려하여 ( 거리 / 시간 ) 비율을 구한다.
   * 2) 특정 거리 이상의 사용자는 1) 번 비율에서 가중치를 부여한다.
   *  5km 미만   : 1.0
   *  5km 이상   : 1.2
   *  10km 이상  : 1.3
   *  21.0975km 이상 : 1.4  (하프마라톤)
   *  42.195km 이상  : 1.5  (마라톤)
   */
//  @Scheduled(cron = "*/10 * * * * *") // 10초에 한번(개발용)
//  @Scheduled(cron = "0 0 0 * * *") // 매일 자정
  @Scheduled(cron = "0 0 * * * *") // 정각마다 수행
  @Transactional
  public void saveRankingByMonth() throws Exception {
    LocalDate currentDate = LocalDate.now();
    Integer year = currentDate.getYear();
    Integer month = currentDate.getMonthValue();

    // 조회년월 랭킹 데이터 삭제 (매일 초기화)
    rankRepository.deleteByYearAndMonth(year, month);

    // 조회년월의 유효한 러너 기록
    List<UserPost> monthRunRecords = userPostRepository.findAllByValidYnIsTrueAndYearAndMonthAndActualEndTimeIsNotNull(
        year,
        month);
    if (monthRunRecords.isEmpty()) {
      return;
    }

    List<RankSaveDto> monthSortRank = monthRunRecords.stream()
        .map(userPost -> {
          double baseScore = calDistanceWeight(userPost);
          return RankSaveDto.builder()
              .userId(userPost.getUser().getId())
              .totalDistance(userPost.getTotalDistance())
              .actualStartTime(userPost.getActualStartTime())
              .actualEndTime(userPost.getActualEndTime())
              .runningDuration(userPost.getRunningDuration())
              .score(baseScore)
              .build();
        })
        .collect(Collectors.groupingBy(RankSaveDto::getUserId,
            Collectors.reducing((dto1, dto2) -> RankSaveDto.builder()
                .userId(dto1.getUserId())
                .totalDistance(dto1.getTotalDistance() + dto2.getTotalDistance()) // 거리 합산
                .runningDuration(
                    dto1.getRunningDuration().plus(dto2.getRunningDuration())) // 러닝 시간 합산
                .score(dto1.getScore() + dto2.getScore()) // 점수 합산
                .build())
        ))
        .values().stream()
        .map(Optional::orElseThrow)
        .sorted(Comparator.comparingDouble(RankSaveDto::getScore).reversed()) // 점수 기준 내림차순 정렬
        .collect(Collectors.toList());

    int rank = 0;
    for (RankSaveDto rankItem : monthSortRank) {
      User user = userRepository.findById(rankItem.getUserId())
          .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

      Rank newRank = new Rank();
      newRank.setUser(user);
      newRank.setYear(year);
      newRank.setMonth(month);
      newRank.setRankPosition(++rank);
      newRank.setTotalDistance(rankItem.getTotalDistance());
      newRank.setTotalTime(rankItem.getRunningDuration());
      rankRepository.save(newRank);
    }

  }

  private double calDistanceWeight(UserPost userPost) {
    double totalTimeInSeconds = userPost.getRunningDuration().toMillis() / 1000.0;
    if (totalTimeInSeconds == 0) {
      return 0;
    }
    double baseScore = userPost.getTotalDistance() / totalTimeInSeconds;
    if (userPost.getTotalDistance() >= 42195) {
      baseScore *= 1.5; // 마라톤
    } else if (userPost.getTotalDistance() >= 21097.5) {
      baseScore *= 1.4; // 하프
    } else if (userPost.getTotalDistance() >= 10000) {
      baseScore *= 1.3; // 10km ~
    } else if (userPost.getTotalDistance() >= 5000) {
      baseScore *= 1.2; // 5km ~
    } else {
      baseScore *= 1.0; // ~ 5km
    }
    return baseScore;
  }
}
