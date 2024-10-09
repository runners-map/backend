package com.service.runnersmap.service;

import com.service.runnersmap.dto.PostDto;
import com.service.runnersmap.dto.PostSearchDto;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.entity.UserPostPK;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.PostRepository;
import com.service.runnersmap.repository.UserPostRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostService {

  private final PostRepository postRepository;

  private final UserPostRepository userPostRepository;

  private final UserRepository userRepository;


  @Transactional(readOnly = true)
  public List<PostDto> searchPost(PostSearchDto inDto) throws Exception {

    return postRepository.findAllWithin1Km(
        inDto.getSwLatlng(),
        inDto.getNeLatlng(),
        inDto.getGender(),
        inDto.getPaceMinStart(),
        inDto.getPaceMinEnd(),
        inDto.getDistanceStart(),
        inDto.getDistanceEnd(),
        inDto.getStartDate(),
        inDto.getStartTime(),
        inDto.getLimitMemberCnt()
    ).stream().map(p -> PostDto.fromEntity(p)).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<Post> searchDetailPost(Long postId) throws Exception {
    return postRepository.findById(postId);
  }

  public PostDto registerPost(PostDto postDto) throws Exception {
    log.info("started to registerPost Title");

    // admin(그룹장)이 유효한 사용자인지 확인
    User user = userRepository.findById(postDto.getAdminId())
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    // 그룹장이 중복된 시간으로 게시글을 등록하지는 않았는지 체크(완료처리되지 않은 건이 동일 일자에 있다면)
    LocalDateTime startDateTime = postDto.getStartDateTime();
    LocalDateTime endDateTime = startDateTime.plusDays(1);
    boolean dupYn = postRepository.existsByAdminIdAndStartDateTimeAndArriveYnFalse(
        postDto.getAdminId(), startDateTime, endDateTime);
    if (dupYn) {
      throw new RunnersMapException(ErrorCode.ALREADY_EXISTS_POST_DATA);
    }

    // post 테이블 insert
    Post post = postRepository.save(Post.builder()
        .adminId(postDto.getAdminId())
        .title(postDto.getTitle())
        .content(postDto.getContent())
        .limitMemberCnt(postDto.getLimitMemberCnt())
        .gender(postDto.getGender())
        .startDateTime(postDto.getStartDateTime())
        .startPosition(postDto.getStartPosition())
        .distance(postDto.getDistance())
        .paceMin(postDto.getPaceMin())
        .paceSec(postDto.getPaceSec())
        .path(postDto.getPath())
        .lat(postDto.getSwLatlng())
        .lng(postDto.getNeLatlng())
        .departureYn(false)
        .arriveYn(false)
        .build());

    // 그룹 사용자에 그룹장을 추가한다.
    UserPost userPost = new UserPost();
    userPost.setId(new UserPostPK(user, post));
    userPost.setValid_yn(true);
    userPostRepository.save(userPost);

    // 채팅방도 동시 개설한다. (추후 구현)

    return PostDto.fromEntity(post);

  }

  public PostDto modifyPost(PostDto postDto) throws Exception {
    Optional<Post> postItem = postRepository.findById(postDto.getPostId());
    if (postItem.isPresent()) {
      Post post = postItem.get();

      // 변경 가능 상태인지 체크
      validatePost(post);

      post.setTitle(postDto.getTitle());
      post.setContent(postDto.getContent());
      post.setLimitMemberCnt(postDto.getLimitMemberCnt());
      post.setGender(postDto.getGender());
      post.setStartDateTime(postDto.getStartDateTime());
      post.setStartPosition(postDto.getStartPosition());
      post.setDistance(postDto.getDistance());
      post.setPaceMin(postDto.getPaceMin());
      post.setPaceSec(postDto.getPaceSec());
      post.setPath(postDto.getPath());
      post.setDepartureYn(postDto.getDepartureYn());
      post.setArriveYn(postDto.getArriveYn());
      postRepository.save(post);

      return PostDto.fromEntity(post);

    } else {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA);
    }
  }

  public void deletePost(Long postId) throws Exception {
    Optional<Post> postItem = postRepository.findById(postId);
    if (postItem.isPresent()) {
      Post post = postItem.get();

      // 변경 가능 상태인지 체크
      validatePost(post);

      postRepository.deleteById(post.getPostId());

      // 채팅방 동시 삭제된다. (추후 구현)

    } else {
      throw new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA);
    }
  }

  private void validatePost(Post post) {
    // 해당 채팅방의 그룹장 이외에는 불가
    if (!post.getAdminId().equals(post.getAdminId())) {
      throw new RunnersMapException(ErrorCode.OWNER_ONLY_ACCESS_POST_DATA);
    }

    // 완료 전의 러닝 글에 대해서만 가능
    if (post.getArriveYn()) {
      throw new RunnersMapException(ErrorCode.ALREADY_COMPLETE_POST_DATA);
    }
  }

}
