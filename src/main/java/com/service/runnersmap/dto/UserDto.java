package com.service.runnersmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUpDto {

    private String email;
    private String password;
    private String nickname;
    private String gender;
    private int paceMin;
    private int paceSec;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class LoginDto {

    private String email;
    private String password;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AccountDeleteDto {
    private String password;
  }
}
