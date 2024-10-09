//package com.service.runnersmap.entity;
//
//import java.time.LocalDateTime;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//
//@Document(indexName = "post_index")
//public class PostDocument {
//
//  @Id
//  private Long postId; //메이트모집글ID
//
//  private String adminId; //그룹장ID ( User랑 조인 )
//
//  private String title; //제목
//
//  private String content; //내용
//
//  private Long limitMemberCount; //제한인원
//
//  private String gender; //모집성별
//
//  private LocalDateTime startDateTime; //출발일시
//
//  private String startPosition;  //출발장소
//
//  private Long distance; // 달릴거리
//
//  private Long paceMin; //예상 페이스분
//
//  private Long paceSec;  //예상 페이스초
//
//  private String paceDistance; //예상 페이스 기준거리
//
//  private String path; //경로
//
//  private boolean departureYn; //출발여부
//
//  private boolean arriveYn; //도착여부
//
//}
