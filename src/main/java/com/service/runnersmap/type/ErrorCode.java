package com.service.runnersmap.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 모집글 관련 에러코드
  NOT_FOUND_POST_DATA("모집글 데이터를 찾을 수 없습니다."),

  ALREADY_DEPARTURE_POST_DATA("이미 시작 처리된 내역입니다."),

  ALREADY_COMPLETE_POST_DATA("이미 완료처리된 내역입니다."),
  
  OWNER_ONLY_ACCESS_POST_DATA("그룹장만 변경할 수 있습니다."),

  ALREADY_EXISTS_POST_DATA("아직 진행 중인 모집내역이 있어 신규 등록 불가합니다."),

  NOT_DEPARTURE_POST_DATA("출발이후에 종료 처리 가능합니다."),

  NOT_FOUND_USER("이용자를 찾을 수 없습니다."),

  NOT_VALID_USER("이미 탈퇴 혹은 강퇴 처리된 사용자입니다."),

  NOT_POST_INCLUDE_USER("모집글에 참여한 사용자가 아니므로 처리 불가합니다."),

  ;

  private final String description;

}
