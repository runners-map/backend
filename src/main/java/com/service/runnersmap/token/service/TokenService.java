package com.service.runnersmap.token.service;

import com.service.runnersmap.token.dto.TokenResponse;

public interface TokenService {
  TokenResponse reissueAccessToken(String authorizationHeader);
}
