package com.service.runnersmap.repository;

import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.entity.UserPostPK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, UserPostPK> {

  @Query("SELECT u FROM UserPost u WHERE u.id.postId.id = :postId AND valid_yn = 1")
  List<UserPost> findByPostId(@Param("postId") Long postId);

}
