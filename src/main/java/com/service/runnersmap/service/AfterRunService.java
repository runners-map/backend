package com.service.runnersmap.service;

import com.service.runnersmap.dto.AfterRunPictureDto;
import com.service.runnersmap.entity.AfterRunPicture;
import com.service.runnersmap.entity.Likes;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.AfterRunPictureRepository;
import com.service.runnersmap.repository.LikesRepository;
import com.service.runnersmap.repository.PostRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AfterRunService {

  private final FileStorageService fileStorageService;
  private final AfterRunPictureRepository afterRunPictureRepository;
  private final LikesRepository likesRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  /**
   * 인증샷 업로드 그룹장이 ‘러닝 종료’ 버튼을 클릭시, 그룹장에게만 인증샷 업로드 버튼이 제공됨
   */
  @Transactional
  public AfterRunPictureDto createAfterRunPicture(Long postId, Long userId, MultipartFile file)
      throws IOException {
    log.info("인증샷 업로드 시도 요청");

    // 모집글 유무 확인
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    // 그룹장 권한 체크
    validateAdminAccess(post, userId);
    // 도착 여부 확인
    validateArrivedAYn(post);

    // S3에 인증샷 업로드 -> url 받아옴
    String afterRunPictureUrl = fileStorageService.uploadFile(file);
    log.info("S3에 인증샷 업로드 완료: {}", afterRunPictureUrl);

    AfterRunPicture afterRunPicture = AfterRunPicture.builder()
        .user(post.getAdmin())
        .post(post)
        .afterRunPictureUrl(afterRunPictureUrl)
        .createdAt(LocalDateTime.now())
        .build();

    AfterRunPicture savedAfterRunPicture = afterRunPictureRepository.save(afterRunPicture);
    return AfterRunPictureDto.builder()
        .fileId(savedAfterRunPicture.getId())
        .afterRunPictureUrl(savedAfterRunPicture.getAfterRunPictureUrl())
        .likeCount(0)
        .build();
  }

  /**
   * 인증샷 조회 - 모든 이용자가 조회 가능 좋아요 수도 반환
   */
  @Transactional(readOnly = true)
  public AfterRunPictureDto viewAfterRunPicture(Long postId) {
    log.info("인증샷 조회 요청 : 모집글Id = {}", postId);
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));

    AfterRunPicture afterRunPicture = afterRunPictureRepository.findByPost(post)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_FILE_DATA));

    int likeCount = likesRepository.countByAfterRunPicture(afterRunPicture);

    return AfterRunPictureDto.builder()
        .afterRunPictureUrl(afterRunPicture.getAfterRunPictureUrl())
        .likeCount(likeCount)
        .build();
  }

  // 그룹장 권한 체크
  private void validateAdminAccess(Post post, Long userId) {
    if (!post.getAdmin().getId().equals(userId)) {
      log.error("그룹장만 인증샷을 업로드할 수 있습니다.");
      throw new RunnersMapException(ErrorCode.OWNER_ONLY_ACCESS_POST_DATA);
    }
  }

  // 도착 여부 확인
  private void validateArrivedAYn(Post post) {
    if (!post.getArriveYn()) {
      log.error("러닝이 아직 종료되지 않았습니다.");
      throw new RunnersMapException(ErrorCode.NOT_FINISHED_RUNNING);
    }
  }



  /**
   * 인증샷에 좋아요 모든 이용자가 누를 수 있음 토글 형식 (두 번 누를 시 좋아요 취소)
   */
  @Transactional
  public void toggleLike(Long fileId, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));

    AfterRunPicture afterRunPicture = afterRunPictureRepository.findById(fileId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_FILE_DATA));

    // 이미 좋아요 눌렀는지 확인
    Optional<Likes> alreadyLiked = likesRepository.findByAfterRunPictureAndUser(afterRunPicture, user);
    // 있으면 취소, 없으면 추가
    if (alreadyLiked.isPresent()) {
      likesRepository.delete(alreadyLiked.get());
      afterRunPicture.setLikeCount(afterRunPicture.getLikeCount() - 1); // 좋아요 수 감소
    } else {
      Likes newlikes = Likes.builder()
          .afterRunPicture(afterRunPicture)
          .user(user)
          .build();
      likesRepository.save(newlikes);
      afterRunPicture.setLikeCount(afterRunPicture.getLikeCount() + 1); // 좋아요 수 증가
    }
    afterRunPictureRepository.save(afterRunPicture);
  }
}



