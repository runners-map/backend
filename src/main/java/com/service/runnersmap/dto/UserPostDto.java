package com.service.runnersmap.dto;

import java.time.LocalDateTime;
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

  private LocalDateTime actualStartTime;

}
