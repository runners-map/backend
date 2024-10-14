package com.service.runnersmap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPostDto {

  private Long postId;

  private Long userId;

  private Double distance;

  private String runningTime;

  private int day;

}
