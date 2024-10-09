package com.service.runnersmap.controller;

import com.service.runnersmap.dto.RecordDto;
import com.service.runnersmap.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

  private final RecordService recordService;

  /*
   * 러닝기록 저장(시작버튼)
   */
  @PostMapping("/start")
  public ResponseEntity<?> startRecord(@RequestBody RecordDto recordDto
  ) throws Exception {
    recordService.startRecord(recordDto);
    return ResponseEntity.ok("러닝이 시작되었습니다.");
  }

  /*
   * 러닝기록 저장(완료버튼)
   */
  @PostMapping("/complete")
  public ResponseEntity<?> completeRecord(@RequestBody RecordDto recordDto
  ) throws Exception {
    return ResponseEntity.ok(recordService.completeRecord(recordDto));
  }

  /*
   * 러닝기록 조회
   */
  @GetMapping
  public ResponseEntity<?> searchRecord(@RequestBody RecordDto recordDto
  ) throws Exception {
    return ResponseEntity.ok(null);
  }
}
