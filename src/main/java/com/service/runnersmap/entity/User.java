package com.service.runnersmap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "users")
public class User extends BaseTimeEntity {
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

  @Column(name = "gender", nullable = false)
  private String gender;

  @Column(name = "custom_time", nullable = false)
  private String customTime;

  @Column(name = "provider", nullable = false)
  private String provider;

  @Column(name = "provider_id", nullable = false)
  private String providerId;

}

