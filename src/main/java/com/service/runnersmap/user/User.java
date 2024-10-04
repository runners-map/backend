package com.service.runnersmap.user;

import com.service.runnersmap.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User extends BaseEntity {
  @Column(name = "users_uuid", columnDefinition = "BINARY(16)", unique = true)
  private UUID userId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "provider", nullable = false)
  private String provider;

  @Column(name = "provider_id", nullable = false)
  private String providerId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", length = 20)
  private LocalDateTime updatedAt;

  private User(UserBuilder builder) {
    this.userId = builder.userId;
    this.name = builder.name;
    this.provider = builder.provider;
    this.providerId = builder.providerId;
  }

  public static class UserBuilder {
    private UUID userId;  // userId 필드 추가
    private String name;
    private String provider;
    private String providerId;

    public UserBuilder userId(UUID userId) {
      this.userId = userId;  // userId 메소드 구현
      return this;
    }

    public UserBuilder name(String name) {
      this.name = name;
      return this;
    }

    public UserBuilder provider(String provider) {
      this.provider = provider;
      return this;
    }

    public UserBuilder providerId(String providerId) {
      this.providerId = providerId;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

}