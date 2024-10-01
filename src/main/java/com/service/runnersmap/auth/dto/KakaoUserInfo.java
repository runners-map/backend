package com.service.runnersmap.auth.dto;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {

  private Map<String, Object> attributes;

  @Override
  public String getProviderId() {
    return attributes.get("id").toString();
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getName() {
    return (String) ((Map) attributes.get("properties")).get("nickname");
  }
}
