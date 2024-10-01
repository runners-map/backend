package com.service.runnersmap.user.repository;

import com.service.runnersmap.user.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  Optional<User> findByUserId(UUID userId);

  User findByProviderId(String providerId);

}
