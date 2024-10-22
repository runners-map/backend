package com.service.runnersmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AfterRunPictureDto {

  private String afterRunPictureUrl;  // 인증샷
  private int likeCount;  // 좋아요 수
}
