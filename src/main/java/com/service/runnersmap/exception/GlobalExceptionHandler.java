package com.service.runnersmap.exception;

import com.service.runnersmap.common.ApiResponse;
import com.service.runnersmap.common.code.BaseErrorCode;
import com.service.runnersmap.token.exception.TokenErrorResult;
import com.service.runnersmap.token.exception.TokenException;
import com.service.runnersmap.user.exception.UserErrorResult;
import com.service.runnersmap.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(TokenException.class)
  public ResponseEntity<ApiResponse<BaseErrorCode>> handleTokenException(TokenException e) {
    TokenErrorResult errorResult = e.getTokenErrorResult();
    return ApiResponse.onFailure(errorResult);
  }
  @ExceptionHandler(UserException.class)
  public ResponseEntity<ApiResponse<BaseErrorCode>> handleUserException(UserException e) {
    UserErrorResult errorResult = e.getUserErrorResult();
    return ApiResponse.onFailure(errorResult);
  }
}
