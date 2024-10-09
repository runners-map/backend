//package com.service.runnersmap.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//import org.springframework.data.elasticsearch.support.HttpHeaders;
//
//
//@Slf4j
//@Configuration
//@EnableElasticsearchRepositories
//public class ElasticSearchConfig extends ElasticsearchConfiguration {
//
//  @Value("${spring.elasticsearch.api-key}")
//  private String apiKey;
//
//  @Value("${spring.elasticsearch.host}")
//  private String host;
//
//  @Value("${spring.elasticsearch.username}")
//  private String username;
//
//  @Value("${spring.elasticsearch.password}")
//  private String password;
//
////  @Override
////  public ClientConfiguration clientConfiguration() {
////    HttpHeaders headers = new HttpHeaders();
////    headers.add("Authorization", "ApiKey " + apiKey);
////    return ClientConfiguration.builder()
////        .connectedTo(host)
////        .usingSsl()
////        .withDefaultHeaders(headers)
////        .build();
////  }
//
//  @Override
//  public ClientConfiguration clientConfiguration() {
//    log.info("host : {}" , host);
//    log.info("username : {}" , username);
//    log.info("password : {}" , password);
//    return ClientConfiguration.builder()
//        .connectedTo(host)
//        .usingSsl()  // SSL 사용
//        .withBasicAuth(username, password)
//        .build();
//  }
//
//}
