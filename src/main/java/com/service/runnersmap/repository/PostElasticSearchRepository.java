//package com.service.runnersmap.repository;
//
//import com.service.runnersmap.entity.PostDocument;
//import java.util.List;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface PostElasticSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
//
//
//  @Query("{\"match\": {\"field\": \"?0\"}}")
//  List<PostDocument> searchByTitle(String keyword);
//
//}
