package com.service.runnersmap.service;

import com.service.runnersmap.dto.PostDto;
import com.service.runnersmap.dto.UserPostDto;
import com.service.runnersmap.dto.UserPostSearchDto;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.PostRepository;
import com.service.runnersmap.repository.UserPostRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserPostService {

  private final PostRepository postRepository;

  private final UserPostRepository userPostRepository;

  private final UserRepository userRepository;

  /*
   * 사용자별 러닝 참여 리스트 조회
   */
  @Transactional(readOnly = true)
  public List<PostDto> listParticipatePost(Long userId) throws Exception {

    return userPostRepository.findAllByUser_IdAndValidYnIsTrueAndActualEndTimeIsNull(userId)
        .stream()
        .map(userPost -> {
          Post post = postRepository.findById(userPost.getPost().getPostId())
              .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));
          return PostDto.fromEntity(post);

        })
        .collect(Collectors.toList());

  }

  /*
   * 러닝 참가하기
   */
  public void participate(Long postId, Long userId) throws Exception {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    if (post.getDepartureYn()) {
      throw new RunnersMapException(ErrorCode.ALREADY_DEPARTURE_POST_DATA);
    }
    if (post.getArriveYn()) {
      throw new RunnersMapException(ErrorCode.ALREADY_COMPLETE_POST_DATA);
    }

    // 유효한 사용자가 참여 중복 처리되지 않도록 한다.
    boolean existYn = userPostRepository.existsByUser_IdAndPost_PostIdAndValidYnIsTrue(userId, postId);
    if (existYn) {
      throw new RunnersMapException(ErrorCode.ALREADY_PARTICIPATE_USER);
    }

    UserPost newUserPost = new UserPost();
    newUserPost.setUser(user);
    newUserPost.setPost(post);
    newUserPost.setValidYn(true);
    newUserPost.setTotalDistance(post.getDistance());
    newUserPost.setYear(post.getStartDateTime().getYear());
    newUserPost.setMonth(post.getStartDateTime().getMonthValue());
    userPostRepository.save(newUserPost);


  }

  /*
   * 러닝 나가기
   */
  public void participateOut(Long postId, Long userId) throws Exception {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    Optional<UserPost> optionalUserPost = userPostRepository.findByUser_IdAndPost_PostIdAndValidYnIsTrue(userId, postId);
    if (optionalUserPost.isPresent()) {
      UserPost userPost = optionalUserPost.get();
      // 실제달린 시간, 유효여부 false 처리
      userPost.setActualEndTime(null);
      userPost.setValidYn(false);
      userPostRepository.save(userPost);

    } else {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_USER_POST_DATA);
    }
  }


  /*
   * 러닝기록 - 시작 버튼
   * 1. post 테이블에 출발 업데이트 처리한다. (1명이라도 출발했다면?)
   * 2. userPost 테이블에 실제 출발 시간을 업데이트 처리한다.
   */
  public void startRecord(Long postId, Long userId) throws Exception {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    if (post.getArriveYn()) {
      throw new RunnersMapException(ErrorCode.ALREADY_COMPLETE_POST_DATA);
    }

    if (!post.getDepartureYn()) {
      // 첫번째로 사용자가 출발 눌렀을 때 post 테이블 update
      post.setDepartureYn(true); // 출발여부
      postRepository.save(post); 
    }

    // 모집글에 참여중인 사용자 조회 (유효한 사용자)
    Optional<UserPost> optionalUserPost = userPostRepository.findByUser_IdAndPost_PostIdAndValidYnIsTrue(userId, postId);
    if (optionalUserPost.isPresent()) {
      UserPost userPost = optionalUserPost.get();
      if(userPost.getActualStartTime() != null) {
        throw new RunnersMapException(ErrorCode.ALREADY_START_POST_DATA);
      }
      userPost.setActualStartTime(LocalDateTime.now());
      userPostRepository.save(userPost);
    }
    else {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_USER_POST_DATA);
    }

  }

  /*
   * 러닝기록 - 메이트들의 각각 러닝 정보 저장(사용자가 각각 도착할때마다 호출된다)
   * 1. post 테이블에 도착완료 업데이트 처리한다.
   * 2. userPost 테이블에 최종 도착 시간, 실제 달린 시간, 최종 달린 거리를 업데이트 처리 한다.
   */
  public void completeRecord(Long postId, Long userId) throws Exception {

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

//    if (!post.getDepartureYn()) {
//      throw new RunnersMapException(ErrorCode.NOT_DEPARTURE_POST_DATA);
//    }
//    if (post.getArriveYn()) {
//      throw new RunnersMapException(ErrorCode.ALREADY_COMPLETE_POST_DATA);
//    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    // userPost 테이블 update
    Optional<UserPost> optionalUserPost = userPostRepository.findByUser_IdAndPost_PostIdAndValidYnIsTrue(userId, postId);
    if (optionalUserPost.isPresent()) {
      UserPost userPost = optionalUserPost.get();
      if(userPost.getActualEndTime() != null) {
        throw new RunnersMapException(ErrorCode.ALREADY_COMPLETE_POST_DATA);
      }
      userPost.setActualEndTime(LocalDateTime.now());
      userPost.setRunningDuration(Duration.between(userPost.getActualStartTime(), LocalDateTime.now()));
      userPostRepository.save(userPost);
      // 미완료 러너 존재여부  -> true : 미도착, false : 도착
      boolean existsIncompleteUser = userPostRepository.existsByPost_PostIdAndValidYnIsTrueAndActualEndTimeIsNull(
          postId
      );
      if (!existsIncompleteUser) {
        // 모든 사용자가 도착하면 도착 처리(post 테이블 도착처리)
        // 만약에 사용자가 모두 도착하지 않았는데 비정상 종료처리가 되어야 한다면 그룹장이 모집글 방삭제를 해야한다.
        post.setArriveYn(true); // 도착여부
        postRepository.save(post);
      }

    } else {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_USER_POST_DATA);
    }
  }


  /*
   * 러닝기록 - 조회
   * 1. ALL   : 누적달린 거리
   * 2. MONTH : 입력받은 월의 총 달린 거리
   * 3. DAY   : 입력받은 월의 달린거리
   */
  @Transactional(readOnly = true)
  public List<UserPostSearchDto> searchRunningData(Long userId, int year, int month)
      throws Exception {

    List<UserPostSearchDto> result = new ArrayList<>();


    result.add(new UserPostSearchDto("ALL", userPostRepository.findTotalDistanceByUserId(userId)));


    double sumMonthTotaldistance = 0;
    List<UserPost> userPosts = userPostRepository.findAllByUserIdAndYearAndMonth(userId, year, month);
    for(UserPost item : userPosts) {
      sumMonthTotaldistance += item.getTotalDistance() == null ? 0 : item.getTotalDistance();
    }
    result.add(new UserPostSearchDto("MONTH", sumMonthTotaldistance));

    List<UserPostDto> runningMonths = userPostRepository.findAllByUser_IdAndValidYnIsTrueAndYearAndMonth(
            userId, year, month)
        .stream()
        .map(up -> new UserPostDto(
            up.getUser().getId(),
            up.getPost().getPostId(),
            up.getTotalDistance(),
            DurationToStringConverter.convert(up.getRunningDuration()),
            up.getActualEndTime().getDayOfMonth(),
            up.getActualStartTime()
        ))
        .sorted(Comparator.comparing(up -> up.getActualStartTime()))
        .collect(Collectors.toList());

    result.add(new UserPostSearchDto("DAY", runningMonths));

    return result;
  }

  public class DurationToStringConverter {

    public static String convert(Duration duration) {
      if (duration == null) {
        return "00:00:00";
      }
      long seconds = duration.getSeconds();
      return String.format("%02d:%02d:%02d",
          (seconds / 3600), // 시간
          (seconds % 3600) / 60, // 분
          (seconds % 60)); // 초
    }
  }

  @Transactional(readOnly = true)
  public String userPostState(Long postId, Long userId) {

    Optional<UserPost> optionalUserPost = userPostRepository.findByUser_IdAndPost_PostIdAndValidYnIsTrue(userId, postId);
    if (optionalUserPost.isPresent()) {

      UserPost userPost = optionalUserPost.get();
      if(!userPost.getValidYn()) {
        throw new RunnersMapException(ErrorCode.NOT_VALID_USER);
      }

      return userPost.getActualStartTime() != null ? "COMPLETE" : "START";
    } else {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_USER_POST_DATA);
    }
  }

}
