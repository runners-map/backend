package com.service.runnersmap.exception;

import com.service.runnersmap.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RunnersMapException extends RuntimeException {

  private ErrorCode errorCode;
  private String ErrorMessage;

  public RunnersMapException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.ErrorMessage = errorCode.getDescription();
  }
}
