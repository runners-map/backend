package com.service.runnersmap.repository;

import com.service.runnersmap.entity.FileStorage;
import com.service.runnersmap.entity.Likes;
import com.service.runnersmap.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

  // 특정 사용자가 특정 인증샷에 공감을 했는지 확인
  Optional<Likes> findByFileStorageAndUser(FileStorage fileStorage, User user);

}
