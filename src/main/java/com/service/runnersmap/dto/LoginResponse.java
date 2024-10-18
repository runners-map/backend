package com.service.runnersmap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  private String accessToken;
  private String refreshToken;
  private Long userId; // 사용자 ID
  private String nickname; // 사용자 닉네임
  private String lastPosition; // 사용자 마지막 위치
}
