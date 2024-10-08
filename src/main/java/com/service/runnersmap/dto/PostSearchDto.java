package com.service.runnersmap.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchDto {

  private Double swLatlng;
  private Double neLatlng;
  private String gender;
  private Integer paceMinStart;
  private Integer paceMinEnd;
  private Long distanceStart;
  private Long distanceEnd;
  private LocalDate startDate;
  private String startTime;
  private Integer limitMemberCnt;

}
