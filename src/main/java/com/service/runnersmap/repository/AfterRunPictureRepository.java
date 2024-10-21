package com.service.runnersmap.repository;

import com.service.runnersmap.entity.AfterRunPicture;
import com.service.runnersmap.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterRunPictureRepository extends JpaRepository<AfterRunPicture, Long> {

  Optional<AfterRunPicture> findByPost(Post post); // 모집글에 해당하는 인증샷 조회

}
