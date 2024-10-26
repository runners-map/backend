package com.service.runnersmap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUpDto {

    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String gender;
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

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AccountInfoDto {

    private String nickname;
    private String email;
    private String gender;
    private String profileImageUrl;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AccountUpdateDto {

    private String newNickname;
    private String newPassword;
    private String newConfirmPassword;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class LastPositionDto {
    private String lastPosition;
  }
}