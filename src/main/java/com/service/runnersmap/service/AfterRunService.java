package com.service.runnersmap.service;

import com.service.runnersmap.entity.FileStorage;
import com.service.runnersmap.entity.Likes;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.exception.RunnersMapException;
import com.service.runnersmap.repository.FilesStorageRepository;
import com.service.runnersmap.repository.LikesRepository;
import com.service.runnersmap.repository.PostRepository;
import com.service.runnersmap.repository.UserRepository;
import com.service.runnersmap.type.ErrorCode;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AfterRunService {

  private final FileStorageService fileStorageService;
  private final FilesStorageRepository filesStorageRepository;
  private final LikesRepository likesRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  /**
   * 인증샷 업로드 그룹장이 ‘러닝 종료’ 버튼을 클릭시, 그룹장에게만 인증샷 업로드 버튼이 제공됨
   */
  public void createAfterRunPhoto(Long postId, User admin, MultipartFile file) throws IOException {

//    // 모집글 유무 확인
//    Post post = postRepository.findById(postId)
//        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_POST_DATA));
//
//    // 그룹장 권한 체크
//    if (!post.getAdmin().getId().equals(admin.getId())) {
//      throw new RunnersMapException(ErrorCode.OWNER_ONLY_ACCESS_POST_DATA);
//    }
//
//    // 도착 여부 확인
//    if (!post.getArriveYn()) {
//      throw new RunnersMapException(ErrorCode.NOT_FINISHED_RUNNING);
//    }
//
//    // 인증샷 업로드
//    fileStorageService.uploadAfterRunPic(file, post);

  }

  /**
   * 인증샷 조회 - 모든 이용자가 조회 가능
   */
  public FileStorage viewAfterRunPhoto(Long fileId) {
    return filesStorageRepository.findById(fileId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_FILE_DATA));
  }

  /**
   * 인증샷에 좋아요
   * 모든 이용자가 누를 수 있음
   * 토글 형식 (두 번 누를 시 좋아요 취소)
   */
  @Transactional
  public void toggleLike(Long fileId, Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_USER));
    FileStorage file = filesStorageRepository.findById(fileId)
        .orElseThrow(() -> new RunnersMapException(ErrorCode.NOT_FOUND_FILE_DATA));

    // 이미 좋아요 눌렀는지 확인
    Optional<Likes> alreadyLiked = likesRepository.findByFileStorageAndUser(file, user);
    // 있으면 취소, 없으면 추가
    if (alreadyLiked.isPresent()) {
      likesRepository.delete(alreadyLiked.get());
      file.setLikeCount(file.getLikeCount() - 1);
    } else {
      Likes newlikes = Likes.builder()
          .fileStorage(file)
          .user(user)
          .build();
      likesRepository.save(newlikes);
      file.setLikeCount(file.getLikeCount() + 1);
    }
    filesStorageRepository.save(file);
  }
}


