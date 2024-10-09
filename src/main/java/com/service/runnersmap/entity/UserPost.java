package com.service.runnersmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "userPost")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UserPost {

  @EmbeddedId
  private UserPostPK id; // 복합 키(postId, userId)

  @Column(nullable = true)
  private Boolean valid_yn; // 유효여부(탈퇴, 강퇴여부)

  @Column(nullable = true)
  private Double totalDistance; // 달린 거리

  @Column(nullable = true)
  private LocalDateTime actualStartTime; //(실제)출발시간

  @Column(nullable = true)
  private LocalDateTime actualEndTime; //(실제)도착시간

  @Column(nullable = true)
  private Duration runningDuration; // 소요시간

  @CreatedDate
  private LocalDateTime createdDateTime;
  @LastModifiedDate
  private LocalDateTime updatedDateTime;

}
