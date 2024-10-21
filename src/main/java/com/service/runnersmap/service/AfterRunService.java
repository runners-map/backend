package com.service.runnersmap.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AfterRunService {

  private final FileStorageService fileStorageService;
  private final AfterRunPictureRepository afterRunPictureRepository;
  private final LikesRepository likesRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

//  /**
//   * 인증샷 업로드 그룹장이 ‘러닝 종료’ 버튼을 클릭시, 그룹장에게만 인증샷 업로드 버튼이 제공됨
//   */
//  public AfterRunPicture createAfterRunPhoto(Long postId, Long userId, MultipartFile file)
//      throws IOException {
//
//    // 모집글 유무 확인
//    Post post = postRepository.findById(postId)
//        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));
//
//    // 그룹장 권한 체크
//    if (!post.getAdmin().getId().equals(userId)) {
//      throw new RunnersMapException(ErrorCode.OWNER_ONLY_ACCESS_POST_DATA);
//    }
//
//    // 도착 여부 확인
//    if (!post.getArriveYn()) {
//      throw new RunnersMapException(ErrorCode.NOT_FINISHED_RUNNING);
//    }
//
//    // S3에 인증샷 업로드 -> url 받아옴
//    String afterRunPictureUrl = fileStorageService.uploadFile(file);
//
//    AfterRunPicture afterRunPicture = AfterRunPicture.builder()
//        .user(post.getAdmin())
//        .post(post)
//        .afterRunPictureUrl(afterRunPictureUrl)
//        .createdAt(LocalDateTime.now())
//        .build();
//
//    return afterRunPictureRepository.save(afterRunPicture);
//  }
//
//  /**
//   * 인증샷 조회 - 모든 이용자가 조회 가능
//   */
//  public AfterRunPicture viewAfterRunPhoto(Long postId) {
//
//    Post post = postRepository.findById(postId)
//        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));
//
//    return afterRunPictureRepository.findByPost(post)
//        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_FILE_DATA));
//  }
//
//  /**
//   * 인증샷에 좋아요 모든 이용자가 누를 수 있음 토글 형식 (두 번 누를 시 좋아요 취소)
//   */
//  @Transactional
//  public void toggleLike(Long fileId, Long userId) {
//
//    User user = userRepository.findById(userId)
//        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));
//
//    AfterRunPicture file = afterRunPictureRepository.findById(fileId)
//        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_FILE_DATA));
//
//    // 이미 좋아요 눌렀는지 확인
//    Optional<Likes> alreadyLiked = likesRepository.findByAfterRunPictureAndUser(file, user);
//    // 있으면 취소, 없으면 추가
//    if (alreadyLiked.isPresent()) {
//      likesRepository.delete(alreadyLiked.get());
//      file.setLikeCount(file.getLikeCount() - 1);
//    } else {
//      Likes newlikes = Likes.builder()
//          .afterRunPicture(file)
//          .user(user)
//          .build();
//      likesRepository.save(newlikes);
//      file.setLikeCount(file.getLikeCount() + 1);
//    }
//    afterRunPictureRepository.save(file);
//  }
}


