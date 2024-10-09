package com.service.runnersmap.entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;
import lombok.Setter;

@Setter
public class UserPostPK implements Serializable {

  private Long userId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "postId", nullable = false)
  private Post postId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserPostPK that = (UserPostPK) o;
    return Objects.equals(userId, that.userId) && Objects.equals(postId, that.postId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, postId);
  }
}
