package com.service.runnersmap.repository;

import com.service.runnersmap.entity.UserPost;
import com.service.runnersmap.entity.UserPostPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, UserPostPK> {

}
