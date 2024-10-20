package com.service.runnersmap.controller;

import com.service.runnersmap.service.FileStorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

  private final FileStorageService fileStorageService;

  /**
   * 파일 업로드
   */
  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      String fileUrl = fileStorageService.uploadFile(file);
      return ResponseEntity.ok(fileUrl);  // 성공 시 업로드된 파일의 URL 반환
    } catch (IOException e) {
      // 에러 발생 시 400 Bad Request 반환
      return ResponseEntity.badRequest().body("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
    }
  }
}
