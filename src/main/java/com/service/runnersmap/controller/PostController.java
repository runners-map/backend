package com.service.runnersmap.controller;

import com.service.runnersmap.dto.PostDto;
import com.service.runnersmap.dto.PostSearchDto;
import com.service.runnersmap.service.PostService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  /*
   * 러닝모집글 내역 조회
   * Elastic Search
   */
  @GetMapping("/all-posts")
  public ResponseEntity<?> searchPost(
      @RequestParam(value = "swLatlng") Double swLatlng,
      @RequestParam(value = "neLatlng") Double neLatlng,
      @RequestParam(value = "gender", required = false) String gender,
      @RequestParam(value = "paceMinStart", required = false) Integer paceMinStart,
      @RequestParam(value = "paceMinEnd", required = false) Integer paceMinEnd,
      @RequestParam(value = "distanceStart", required = false) Long distanceStart,
      @RequestParam(value = "distanceEnd", required = false) Long distanceEnd,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "startTime", required = false) String startTime,
      @RequestParam(value = "limitMemberCnt", required = false) Integer limitMemberCnt
  ) throws Exception {

    PostSearchDto inDto = new PostSearchDto();
    inDto.setSwLatlng(swLatlng);
    inDto.setNeLatlng(neLatlng);
    inDto.setGender(gender);
    inDto.setPaceMinStart(paceMinStart);
    inDto.setPaceMinEnd(paceMinEnd);
    inDto.setDistanceStart(distanceStart);
    inDto.setDistanceEnd(distanceEnd);
    inDto.setStartDate(startDate);
    inDto.setStartTime(startTime);
    inDto.setLimitMemberCnt(limitMemberCnt);

    return ResponseEntity.ok(postService.searchPost(inDto));
  }


  /*
   * 러닝모집글 상세조회
   * Elastic Search
   */
  @GetMapping
  public ResponseEntity<?> searchDetailPost(
      @RequestParam(value = "postId") Long postId
  ) throws Exception {
    return ResponseEntity.ok(postService.searchDetailPost(postId));
  }

  /*
   * 러닝모집글 신규 등록
   */
  @PostMapping
  public ResponseEntity<?> registerPost(@RequestBody PostDto postDto
  ) throws Exception {
    return ResponseEntity.ok(postService.registerPost(postDto));
  }

  /*
   * 러닝모집글 수정
   */
  @PutMapping
  public ResponseEntity<?> modifyPost(@RequestBody PostDto postDto
  ) throws Exception {
    return ResponseEntity.ok(postService.modifyPost(postDto));
  }

  /*
   * 러닝모집글 삭제
   */
  @DeleteMapping
  public ResponseEntity<?> deletePost(
      @RequestParam(value = "postId") Long postId
  ) throws Exception {
    postService.deletePost(postId);
    return ResponseEntity.ok("삭제되었습니다.");
  }

}
