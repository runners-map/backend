package com.service.runnersmap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "users_id")
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "gender")
  private String gender;

  // 페이스
  private int paceMin;
  private int paceSec;

  // 누적거리
  private double totalDistance;

  // 마지막 위치
  private String lastPosition;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;


// 알림 시간
//  @Column(name = "custom_time")
//  private String customTime;

// Oauth 관련 컬럼
//  @Column(name = "provider")
//  private String provider;
//
//  @Column(name = "provider_id")
//  private String providerId;

}

