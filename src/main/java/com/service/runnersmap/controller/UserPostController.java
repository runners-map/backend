package com.service.runnersmap.controller;

import com.service.runnersmap.dto.PostSearchDto;
import com.service.runnersmap.dto.UserPostDto;
import com.service.runnersmap.service.UserPostService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserPostController {

  private final UserPostService userPostService;

  /*
   * 러닝기록 저장(시작버튼)
   */
  @PostMapping("/api/record/start")
  public ResponseEntity<?> startRecord(
      @RequestParam(value = "postId") Long postId
  ) throws Exception {
    userPostService.startRecord(postId);
    return ResponseEntity.ok("러닝이 시작되었습니다.");
  }

  /*
   * 러닝기록 저장(완료버튼)
   */
  @PostMapping("/api/record/complete")
  public ResponseEntity<?> completeRecord(@RequestBody UserPostDto recordDto
  ) throws Exception {
    return ResponseEntity.ok(userPostService.completeRecord(recordDto));
  }

  /*
   * 러닝기록 조회
   */
  @GetMapping("/api/record")
  public ResponseEntity<?> searchRunningData(
      @RequestParam(value = "userId") Long userId,
      @RequestParam(value = "year") int year,
      @RequestParam(value = "month") int month
  ) throws Exception {
    return ResponseEntity.ok(userPostService.searchRunningData(userId, year, month));
  }
}
