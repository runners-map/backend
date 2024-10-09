package com.service.runnersmap.service;

import com.service.runnersmap.dto.UserPostDto;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.PostRepository;
import com.service.runnersmap.repository.RecordRepository;
import com.service.runnersmap.repository.UserPostRepository;
import com.service.runnersmap.type.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordService {

  private final RecordRepository recordRepository;

  private final PostRepository postRepository;

  private final UserPostRepository userPostRepository;

  /*
   * 러닝기록 - 시작 버튼 (그룹장권한 / 한번 호출로 모든 메이트들의 정보를 생성한다)
   * 1. post 테이블에 출발 업데이트 처리한다.
   * 2. record 테이블에 실제 출발 시간을 저장한다.
   */
  public void startRecord(UserPostDto recordDto) throws Exception {

    Post post = postRepository.findById(recordDto.getPostId())
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    // post 테이블 update
    post.setDepartureYn(true); // 출발여부
    postRepository.save(post);

    // 모집글에 참여중인 사용자 조회 (유효한 사용자)
    List<UserPost> userList = userPostRepository.findByPostId(recordDto.getPostId());
    if(userList == null || userList.size() <= 0) {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_USER);
    }

    // record 테이블 insert
    for(UserPost userItem : userList) {
      Record record = Record.builder()
          .postId(post)
          .userId(userItem.getId().getUserId())
          .runningDistance(recordDto.getRunningDistance())
          .startDateTime(LocalDateTime.now())
          .build();
      recordRepository.save(record);
    }

  }

  /*
   * 러닝기록 - 메이트들의 각각 러닝 정보 저장(도착할때마다 호출된다)
   * 1. post 테이블에 도착완료 업데이트 처리한다.
   * 2. record 테이블에 최종 도착시간, 최종 달린 거리를 저장한다.
   */
  public Record completeRecord(UserPostDto recordDto) throws Exception {

    Post post = postRepository.findById(recordDto.getPostId())
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    if(!post.getDepartureYn()) {
      throw new RunnersMapException(ErrorCode.NOT_DEPARTURE_POST_DATA);
    }

    if(post.getArriveYn()) {
      throw new RunnersMapException(ErrorCode.ALREADY_COMPLETE_POST_DATA);
    }

    // post 테이블 update
    post.setArriveYn(true); // 도착여부
    postRepository.save(post);

    // record 테이블 update

    Record record = recordRepository.findByPostIdAnd(recordDto.getPostId(), recordDto.getUserId());

//    Record record = Record.builder()
//        .postId(post)
//        .runningDistance(recordDto.getRunningDistance())
//        .endDateTime(LocalDateTime.now())
//        .build();

    recordRepository.save(record);

    return record;
  }


}
