package com.service.runnersmap.repository;

import com.service.runnersmap.entity.RefreshToken;
import com.service.runnersmap.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


  void deleteByUser(User user);

  Optional<RefreshToken> findByToken(String token);
}
