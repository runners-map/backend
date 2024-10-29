//package com.service.runnersmap.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import com.service.runnersmap.dto.PostDto;
//import com.service.runnersmap.repository.PostRepository;
//import java.time.LocalDateTime;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class PostServiceTest {
//
//  @InjectMocks
//  private PostService postService;
//
//  @Mock
//  private PostRepository postRepository;
//
//  @BeforeEach
//  public void setup() {
//    MockitoAnnotations.openMocks(this);
//  }
//
//  @Test
//  @DisplayName("모집글 생성 테스트")
//  void registerPostService() throws Exception {
//    //given
//    PostDto postDto = PostDto.builder()
//        .postId(1L)
//        .title("[10/04 모집글]")
//        .content("한강 러닝 모집합니다.(정정)")
//        .limitMemberCnt(3L)
//        .swLatlng(33.452)
//        .neLatlng(126.56)
//        .gender("M")
//        .startDateTime(LocalDateTime.of(2024, 10, 5, 0, 0))
//        .adminId(12L)
//        .build();
//
////
////    Post post = new Post();
////    post.setAdminId(1L);
////    post.setTitle("[10/04 모집글]");
////    post.setContent("한강 러닝 모집합니다.(정정)");
////    post.setLimitMemberCnt(12L);
////    post.setLat(33.452);
////    post.setLng(126.56);
////    post.setGender("M");
////    post.setStartDateTime(LocalDateTime.of(2024, 10, 5, 0, 0));
////    postRepository.save(post);
//
////    given(postRepository.existsByAdminIdAndStartDateTimeAndArriveYnFalse(anyLong(), any(), any()))
////        .willReturn(false);
//
////    given(postRepository.save(any(Post.class)))
////        .willReturn(post);
//
////    ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
//
//    //when
//    PostDto postDto2 = postService.registerPost(postDto);
//
//    //then
////    verify(postRepository, times(1)).save(captor.capture());
//
//    assertEquals(1L, postDto.getPostId());
//    assertEquals("10/04 모집글]", postDto.getTitle());
//
//  }
//}