package com.service.runnersmap.dto;

import com.service.runnersmap.entity.Rank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankDto {

  private Integer rankPosition;

  private Long userId;

  private String nickName;

  private double totalDistance;

  private String totalTime;


  public static RankDto fromEntity(Rank rank) {
    return RankDto.builder()
        .rankPosition(rank.getRankPosition())
        .userId(rank.getUser().getId())
        .nickName(rank.getUser().getNickname())
        .totalDistance(rank.getTotalDistance())
        .totalTime(rank.getTotalTime())
        .build();
  }
}
