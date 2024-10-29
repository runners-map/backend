//package com.service.runnersmap.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.service.runnersmap.dto.PostDto;
//import com.service.runnersmap.service.PostService;
//import java.util.Collections;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(PostController.class)
//class PostControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private PostService postService; //실제 구현 대신 Mock 객체를 사용
//
//  @Autowired
//  private ObjectMapper objectMapper;
//
//  @Test
//  @DisplayName("모집글 목록 조회")
//  public void search_post() throws Exception {
//    // given
//    PostDto postDto = PostDto.builder()
//        .postId(1L)
//        .title("[10/04 모집글]")
//        .content("한강 러닝 모집합니다.")
//        .limitMemberCnt(3L)
//        .swLatlng(33.452)
//        .neLatlng(126.56)
//        .gender("M")
//        .build();
//
//    // when
//    Mockito.when(postService.searchPost(Mockito.any()))
//        .thenReturn(Collections.singletonList(postDto));
//
//    // then
//    mockMvc.perform(get("/api/posts/all-posts")
//            .param("swLatlng", String.valueOf(33.452))   //필수
//            .param("neLatlng", String.valueOf(126.56))          //필수
//            .param("gender", "M")
//        )
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$[0].postId").value(postDto.getPostId()))
//        .andExpect(jsonPath("$[0].title").value(postDto.getTitle()))
//        .andExpect(jsonPath("$[0].content").value(postDto.getContent()))
//        .andExpect(jsonPath("$[0].gender").value(postDto.getGender()))
//        ;
//
//  }
//
//  @Test
//  @DisplayName("모집글 등록")
//  public void registerPost() throws Exception {
//    // given
//    PostDto postDto = PostDto.builder()
//        .postId(1L)
//        .title("[10/04 모집글]")
//        .content("한강 러닝 모집합니다.")
//        .limitMemberCnt(3L)
//        .swLatlng(33.452)
//        .neLatlng(126.56)
//        .gender("M")
//        .build();
//
//    // when
//    Mockito.when(postService.registerPost(Mockito.any(PostDto.class)))
//        .thenReturn(postDto);
//
//    // then
//    mockMvc.perform(post("/api/posts")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(postDto)))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.postId").value(postDto.getPostId()))
//        .andExpect(jsonPath("$.title").value(postDto.getTitle()))
//        .andExpect(jsonPath("$.content").value(postDto.getContent()))
//        .andExpect(jsonPath("$.limitMemberCnt").value(postDto.getLimitMemberCnt()))
//        .andExpect(jsonPath("$.swLatlng").value(postDto.getSwLatlng()))
//        .andExpect(jsonPath("$.neLatlng").value(postDto.getNeLatlng()));
//
//  }
//
//  @Test
//  @DisplayName("모집글 수정")
//  public void modifyPost() throws Exception {
//    // given
//    PostDto postDto = PostDto.builder()
//        .postId(1L)
//        .title("[10/04 모집글]")
//        .content("한강 러닝 모집합니다.(정정)")
//        .limitMemberCnt(3L)
//        .swLatlng(33.452)
//        .neLatlng(126.56)
//        .gender("M")
//        .build();
//
//    // when
//    Mockito.when(postService.modifyPost(Mockito.any(PostDto.class)))
//        .thenReturn(postDto);
//
//    // then
//    mockMvc.perform(put("/api/posts")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(postDto)))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.postId").value(postDto.getPostId()))
//        .andExpect(jsonPath("$.title").value(postDto.getTitle()))
//        .andExpect(jsonPath("$.content").value(postDto.getContent()))
//        .andExpect(jsonPath("$.limitMemberCnt").value(postDto.getLimitMemberCnt()))
//        .andExpect(jsonPath("$.swLatlng").value(postDto.getSwLatlng()))
//        .andExpect(jsonPath("$.neLatlng").value(postDto.getNeLatlng()));
//
//  }
//
//  @Test
//  @DisplayName("모집글 삭제")
//  public void deletePost() throws Exception {
//    // given
//    Long postId = 1L;
//
//    // when
//
//    // then
//    mockMvc.perform(delete("/api/posts")
//            .param("postId", String.valueOf(postId)))
//        .andExpect(status().isOk());
//  }
//}