package com.service.runnersmap.token.repository;

import com.service.runnersmap.token.RefreshToken;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
  RefreshToken findByUserId(UUID userId);

  @Transactional
  @Modifying
  void deleteByUserId(UUID userId);
}
