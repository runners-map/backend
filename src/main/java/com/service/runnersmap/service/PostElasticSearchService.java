//package com.service.runnersmap.service;
//
//import com.service.runnersmap.entity.PostDocument;
//import com.service.runnersmap.repository.PostElasticSearchRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class PostElasticSearchService {
//
//  private final PostElasticSearchRepository postSearchRepository;
//
//  public Iterable<PostDocument> searchByTitle(String keyword) {
//    return postSearchRepository.searchByTitle(keyword);
//  }
//}