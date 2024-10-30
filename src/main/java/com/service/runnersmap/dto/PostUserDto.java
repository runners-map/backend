package com.service.runnersmap.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserDto {

  private Long userId;

  private String nickname;

  private String profileImageUrl;

}
