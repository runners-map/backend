package com.service.runnersmap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 모집글 관련 에러코드
  NOT_FOUND_POST_DATA("모집글 데이터를 찾을 수 없습니다."),

  ALREADY_COMPLETE_POST_DATA("완료처리된 내역은 변경할 수 없습니다."),
  
  OWNER_ONLY_ACCESS_POST_DATA("그룹장만 변경할 수 있습니다."),

  ALREADY_EXISTS_POST_DATA("아직 진행 중인 모집내역이 있어 신규 등록 불가합니다.")

  ;

  private final String description;

}
