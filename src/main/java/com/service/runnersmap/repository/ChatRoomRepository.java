package com.service.runnersmap.repository;

import com.service.runnersmap.entity.ChatRoom;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  Optional<ChatRoom> findById(Long id);

  Optional<ChatRoom> findByPostId(Long postId);
}
