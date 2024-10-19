package com.service.runnersmap.dto;

import com.service.runnersmap.entity.User;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankSaveDto {

  private Long userId;
  private double totalDistance;
  private LocalDateTime actualStartTime; //(실제)출발시간
  private LocalDateTime actualEndTime; //(실제)도착시간
  private Duration runningDuration; // 소요시간
  private double score; // 점수

  public double getTotalTimeInSeconds() {
    return runningDuration.toMillis() / 1000.0;
  }
}
