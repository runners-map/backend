package com.service.runnersmap.dto;

import com.service.runnersmap.entity.UserPost;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPostSearchDto {

  private String type;

  private Object resultList;

}
