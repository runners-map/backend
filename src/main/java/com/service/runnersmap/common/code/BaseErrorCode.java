package com.service.runnersmap.common.code;

import com.service.runnersmap.common.dto.ErrorReasonDto;

public interface BaseErrorCode {
  public ErrorReasonDto getReason();

  public ErrorReasonDto getReasonHttpStatus();

}
