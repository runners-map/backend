package com.service.runnersmap.exception;

import com.service.runnersmap.exception.custom.UnAuthorizedException;
import com.service.runnersmap.exception.custom.UserNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RunnersMapExceptionHandler {

  @ExceptionHandler(RunnersMapException.class)
  public ResponseEntity<Map<String, String>> handleRunnersMapException(RunnersMapException e) {
    log.error("RunnersMapException: {}", e.getMessage());
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", e.getErrorMessage() != null ? e.getErrorMessage() : e.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
    log.error("General Exception: {}", e.getMessage());
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", e.getMessage());
    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(UnAuthorizedException.class)
  public ResponseEntity<String> handleUnAuthorizedException(UnAuthorizedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
