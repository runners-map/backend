package com.service.runnersmap.repository;

import com.service.runnersmap.entity.FileStorage;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesStorageRepository extends JpaRepository<FileStorage, Long> {

  FileStorage findByUser(User user);
  FileStorage findByPost(Post post);

}
