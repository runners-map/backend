package com.service.runnersmap.controller;

import com.service.runnersmap.dto.AfterRunPictureDto;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.service.AfterRunService;
import com.service.runnersmap.type.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts/{postId}/after-run-pictures")
@RequiredArgsConstructor
public class AfterRunController {

  private final AfterRunService afterRunService;
  private final UserRepository userRepository;

  // 인증샷 업로드
  @PostMapping
  public ResponseEntity<AfterRunPictureDto> uploadAfterRunPicture(
      @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long postId,
      @RequestParam("file") MultipartFile file) throws IOException {

    User user = getCurrentUser(userDetails);
    AfterRunPictureDto afterRunPictureDto = afterRunService.createAfterRunPicture(postId, user.getId(), file);
    return ResponseEntity.status(HttpStatus.CREATED).body(afterRunPictureDto);  // 201 Created
  }

  // 인증샷 조회
  @GetMapping
  @PreAuthorize("permitAll()")
  public ResponseEntity<AfterRunPictureDto> getAfterRunPicture(
      @PathVariable Long postId) {
    AfterRunPictureDto afterRunPictureDto = afterRunService.viewAfterRunPicture(postId);
    return ResponseEntity.ok(afterRunPictureDto); // 200 OK
  }

  // 좋아요
  @PostMapping("/{fileId}/like")
  public ResponseEntity<Void> toggleLike(
      @PathVariable Long postId,
      @PathVariable Long fileId,
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = getCurrentUser(userDetails);
    afterRunService.toggleLike(fileId, user.getId());
    return ResponseEntity.noContent().build();  // 204 No Content
  }

  private User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));
  }
}
