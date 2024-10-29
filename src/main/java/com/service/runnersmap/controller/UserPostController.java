package com.service.runnersmap.controller;

import com.service.runnersmap.dto.PostDto;
import com.service.runnersmap.dto.UserPostSearchDto;
import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.service.UserPostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class UserPostController {

  private final UserPostService userPostService;

  /*
   * 러닝모집글 리스트 조회 (사용자별 러닝참여 리스트 조회)
   */
  @GetMapping("/list")
  public ResponseEntity<List<PostDto>> listParticipatePost(
      @RequestParam(value = "userId") Long userId
  ) throws Exception {
    return ResponseEntity.ok(
        userPostService.listParticipatePost(userId)
    );
  }

  /*
   * 러닝 참가하기
   */
  @PostMapping("/participate")
  public ResponseEntity<Void> participate(
      @RequestParam(value = "postId") Long postId,
      @RequestParam(value = "userId") Long userId
  ) throws Exception {
    userPostService.participate(postId, userId);
    return ResponseEntity.ok().build();

  }

  /*
   * 러닝 나가기
   */
  @DeleteMapping("/participate-out")
  public ResponseEntity<Void> participateOut(
      @RequestParam(value = "postId") Long postId,
      @RequestParam(value = "userId") Long userId
  ) throws Exception {
    userPostService.participateOut(postId, userId);
    return ResponseEntity.ok().build();
  }

  /*
   * 러닝기록 저장(시작버튼)
   */
  @PostMapping("/record/start")
  public ResponseEntity<Void> startRecord(
      @RequestParam(value = "postId") Long postId,
      @RequestParam(value = "userId") Long userId
  ) throws Exception {
    userPostService.startRecord(postId, userId);
    return ResponseEntity.ok().build();
  }

  /*
   * 러닝기록 저장(완료버튼)
   */
  @PostMapping("/record/complete")
  public ResponseEntity<UserPost> completeRecord(
      @RequestParam(value = "postId") Long postId,
      @RequestParam(value = "userId") Long userId
  ) throws Exception {
    userPostService.completeRecord(postId, userId);
    return ResponseEntity.ok().build();
  }

  /*
   * 러닝기록 조회
   */
  @GetMapping("/record")
  public ResponseEntity<List<UserPostSearchDto>> searchRunningData(
      @RequestParam(value = "userId") Long userId,
      @RequestParam(value = "year") int year,
      @RequestParam(value = "month") int month
  ) throws Exception {
    return ResponseEntity.ok(
        userPostService.searchRunningData(userId, year, month)
    );
  }


  /*
   * 모집글 사용자 상태조회 [polling]
   * - 사용자별 도착버튼 활성화 여부
   */
  @GetMapping("/userpost-state")
  public ResponseEntity<String> userPostState(
      @RequestParam(value = "postId") Long postId,
      @RequestParam(value = "userId") Long userId
  ) throws Exception {
    return ResponseEntity.ok(userPostService.userPostState(postId, userId));
  }
}
