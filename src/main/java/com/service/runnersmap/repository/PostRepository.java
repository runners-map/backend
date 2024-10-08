package com.service.runnersmap.repository;

import com.service.runnersmap.entity.Post;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  @Query(value =
      "SELECT * FROM post p " +
      "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(p.lat)) " +
      "* cos(radians(p.lng) - radians(:lng)) " +
      "+ sin(radians(:lat)) * sin(radians(p.lat)))) < 1 " +
      "AND (:gender IS NULL OR p.gender = :gender) " +
      "AND (:paceMinStart IS NULL OR (p.pace_min + p.pace_sec / 60) >= :paceMinStart) " +
      "AND (:paceMinEnd IS NULL OR (p.pace_min + p.pace_sec / 60) <= :paceMinEnd) " +
      "AND (:distanceStart IS NULL OR p.distance >= :distanceStart) " +
      "AND (:distanceEnd IS NULL OR p.distance <= :distanceEnd) " +
      "AND (:startDate IS NULL OR (p.start_date_time BETWEEN :startDate AND DATE_ADD(:startDate, INTERVAL 1 DAY))) " +
      "AND (:startTime IS NULL OR TIME_FORMAT(p.start_date_time, '%H%i') = :startTime) " +
      "AND (:limitMemberCnt IS NULL OR p.limit_member_cnt = :limitMemberCnt) " +
      "LIMIT 20",
      nativeQuery = true)
  List<Post> findAllWithin1Km(
      @Param("lat") double lat,
      @Param("lng") double lng,
      @Param("gender") String gender,
      @Param("paceMinStart") Integer paceMinStart,
      @Param("paceMinEnd") Integer paceMinEnd,
      @Param("distanceStart") Long distanceStart,
      @Param("distanceEnd") Long distanceEnd,
      @Param("startDate") LocalDate startDate,
      @Param("startTime") String startTime,
      @Param("limitMemberCnt") Integer limitMemberCnt
  );

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
      "FROM Post p " +
      "WHERE p.adminId = :adminId " +
      "AND p.startDateTime >= :startDateTime " +
      "AND p.startDateTime < :endDateTime " +
      "AND (p.arriveYn IS NULL OR p.arriveYn = false)")
  boolean existsByAdminIdAndStartDateTimeAndArriveYnFalse(
      @Param("adminId") Long adminId,
      @Param("startDateTime") LocalDateTime startDateTime,
      @Param("endDateTime") LocalDateTime endDateTime);
}
