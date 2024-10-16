package com.service.runnersmap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "users_id")
  private Long id;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "gender")
  private String gender;

  // 누적거리
  private double totalDistance;

  // 마지막 위치
  private String lastPosition;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private FileStorage profileImage;

}

