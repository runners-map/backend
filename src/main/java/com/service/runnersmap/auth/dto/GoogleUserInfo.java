package com.service.runnersmap.auth.dto;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

  private Map<String, Object> attributes;

  @Override
  public String getProviderId() {
    return (String) attributes.get("sub");
  }

  @Override
  public String getProvider() {
    return "google";
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }
}
