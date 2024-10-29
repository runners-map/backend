package com.service.runnersmap.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AfterRunPictureDto {

  private Long fileId; // 인증샷 Id
  private String afterRunPictureUrl;  // 인증샷
  private int likeCount;  // 좋아요 수
  private List<Long> likeUserIds; // 좋아요 누른 사용자 id
}
