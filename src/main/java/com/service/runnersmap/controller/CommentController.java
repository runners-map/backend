package com.service.runnersmap.controller;

import com.service.runnersmap.dto.CommentDto;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.service.CommentService;
import com.service.runnersmap.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final UserRepository userRepository;

  // 댓글 작성
  @PostMapping("/{postId}")
  public ResponseEntity<CommentDto> createComment(
      @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long postId,
      @RequestBody CommentDto commentDto)  {

    User user = getCurrentUser(userDetails);

    CommentDto createdComment = commentService.createComment(postId, user.getId(), commentDto);
    return ResponseEntity.status((HttpStatus.CREATED)).body(createdComment); //201 Created
  }


  // 댓글 수정
  @PatchMapping("/{commentId}")
  public ResponseEntity<CommentDto> updateComment(
      @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable Long commentId,
      @RequestBody CommentDto commentDto) {

    User user = getCurrentUser(userDetails);

    CommentDto updatedComment = commentService.updateComment(commentId, user.getId(), commentDto);
    return ResponseEntity.ok(updatedComment); // 200 OK
  }


  // 댓글 삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetails userDetails) {

    User user = getCurrentUser(userDetails);

    commentService.deleteComment(commentId, user.getId());
    return ResponseEntity.noContent().build(); // 204 No Content
  }


  // 댓글 조회
  @GetMapping("/{postId}")
  public ResponseEntity<Page<CommentDto>> getComments(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    User user = getCurrentUser(userDetails);

    Page<CommentDto> commentDtos = commentService.getComments(postId, user.getId(),
        PageRequest.of(page, size));
    return ResponseEntity.ok(commentDtos);  // 200 OK
  }

  // 사용자 정보 조회
  private User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername();
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));
  }
}

