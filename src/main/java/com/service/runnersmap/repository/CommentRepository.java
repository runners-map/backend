package com.service.runnersmap.repository;

import com.service.runnersmap.entity.Comment;
import com.service.runnersmap.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


  Page<Comment> findByPost(Post post, Pageable pageable);
}
