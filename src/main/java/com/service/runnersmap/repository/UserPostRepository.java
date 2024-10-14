package com.service.runnersmap.repository;

import com.service.runnersmap.dto.UserPostDto;
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

  boolean existsById(UserPostPK id);

  @Query("SELECT up FROM UserPost up WHERE up.id.postId = :postId AND up.valid_yn = true")
  List<UserPost> findAllByPostId(@Param("postId") Long postId);

  @Query("SELECT SUM(up.totalDistance) FROM UserPost up WHERE up.id.userId = :userId AND up.valid_yn = true")
  Double findTotalDistanceByUserId(@Param("userId") Long userId);

  @Query("SELECT SUM(up.totalDistance) FROM UserPost up WHERE up.id.userId = :userId AND up.valid_yn = true AND FUNCTION('YEAR', up.actualEndTime) = :year AND FUNCTION('MONTH', up.actualEndTime) = :month")
  Double findTotalDistanceByUserIdAndMonth(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

  @Query("SELECT up " +
         "FROM  UserPost up " +
         "WHERE up.id.userId = :userId "+
         "AND   up.valid_yn     = true " +
         "AND   FUNCTION('YEAR',  up.actualEndTime) = :year " +
         "AND   FUNCTION('MONTH', up.actualEndTime) = :month " +
         "ORDER BY up.actualEndTime ASC")
  List<UserPost> findAllTotalDistanceByUserId(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

}
