package com.service.runnersmap.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordDto {

  private Long postId;

  private Long userId;

  private Long runningDistance;

}
