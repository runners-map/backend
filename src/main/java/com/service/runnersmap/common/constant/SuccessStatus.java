package com.service.runnersmap.common.constant;

import com.service.runnersmap.common.code.BaseCode;
import com.service.runnersmap.common.dto.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

  _OK(HttpStatus.OK, "200", "성공입니다."),
  _CREATED(HttpStatus.CREATED, "201", "생성에 성공했습니다."),

  _CREATED_ACCESS_TOKEN(HttpStatus.CREATED, "201", "액세스 토큰 재발행에 성공했습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;

  @Override
  public ReasonDto getReason() {
    return ReasonDto.builder()
        .isSuccess(true)
        .code(code)
        .message(message)
        .build();
  }

  @Override
  public ReasonDto getReasonHttpStatus() {
    return ReasonDto.builder()
        .isSuccess(true)
        .httpStatus(httpStatus)
        .code(code)
        .message(message)
        .build();
  }
}
