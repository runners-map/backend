package com.service.runnersmap.repository;

import com.service.runnersmap.entity.Post;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query(value =
          "SELECT * " +
          "FROM post p " +
          "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(p.lat)) " +
          "* cos(radians(p.lng) - radians(:lng)) " +
          "+ sin(radians(:lat)) * sin(radians(p.lat)))) < 2 " +
          "AND ( (p.start_date_time >= CURRENT_TIMESTAMP AND p.arrive_yn = false) " +
          "   OR ( p.start_date_time >= DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY) "
                + " AND p.start_date_time <= CURRENT_TIMESTAMP "
                + " AND p.arrive_yn = true) ) " +
          "AND (:gender IS NULL OR p.gender = :gender) " +
          "AND (:paceMinStart IS NULL OR (p.pace_min + p.pace_sec / 60) >= :paceMinStart) " +
          "AND (:paceMinEnd IS NULL OR (p.pace_min + p.pace_sec / 60) <= :paceMinEnd) " +
          "AND (:distanceStart IS NULL OR p.distance >= :distanceStart) " +
          "AND (:distanceEnd IS NULL OR p.distance <= :distanceEnd) " +
          "AND (:startDate IS NULL OR (p.start_date_time BETWEEN :startDate AND DATE_ADD(:startDate, INTERVAL 1 DAY))) " +
          "AND (:startTime IS NULL OR TIME_FORMAT(p.start_date_time, '%H%i') = :startTime) " +
          "AND (:limitMemberCntStart IS NULL OR p.limit_member_cnt >= :limitMemberCntStart)" +
          "AND (:limitMemberCntEnd IS NULL OR p.limit_member_cnt <= :limitMemberCntEnd)" +
          "ORDER BY p.start_date_time ASC " +
          "LIMIT 20",
      nativeQuery = true)
  List<Post> findAllWithin2Km(
      @Param("lat") double lat,
      @Param("lng") double lng,
      @Param("gender") String gender,
      @Param("paceMinStart") Integer paceMinStart,
      @Param("paceMinEnd") Integer paceMinEnd,
      @Param("distanceStart") Long distanceStart,
      @Param("distanceEnd") Long distanceEnd,
      @Param("startDate") LocalDate startDate,
      @Param("startTime") String startTime,
      @Param("limitMemberCntStart") Integer limitMemberCntStart,
      @Param("limitMemberCntEnd") Integer limitMemberCntEnd
  );


  boolean existsByAdminIdAndArriveYnIsFalse(Long adminId);

}
