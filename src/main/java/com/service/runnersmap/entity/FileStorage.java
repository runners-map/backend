package com.service.runnersmap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileStorage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "users_id")
  private User user;  // 업로더

  @OneToOne
  @JoinColumn(name = "post_id")
  private Post post;  // 인증샷과 연관된 모집글

  private String originalFileName;  // 원래 파일명
  private String storedFileName;  // 저장된 파일명
  private String fileUrl;  // 파일 저장 경로
  private Long fileSize;  // 파일 크기(바이트)

  private Integer likeCount = 0;  // 좋아요 수

}
