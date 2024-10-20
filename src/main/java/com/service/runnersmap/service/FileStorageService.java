package com.service.runnersmap.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.service.runnersmap.entity.FileStorage;
import com.service.runnersmap.entity.Post;
import com.service.runnersmap.entity.User;
import com.service.runnersmap.repository.FilesStorageRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;


  private final FilesStorageRepository filesStorageRepository;

  /**
   * S3에 이미지 업로드
   */
  public String uploadFile(MultipartFile file) throws IOException {

    if (file.isEmpty()) {
      throw new IOException("파일이 존재하지 않습니다.");
    }

    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 고유한 파일 이름 생성

    // 메타데이터 설정
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());

    // S3에 파일 업로드 요청 생성
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata);

    try {
      // S3에 파일 업로드
      amazonS3.putObject(putObjectRequest);
    } catch (AmazonServiceException e) {
      log.error("Amazon Service Exception: {}", e.getMessage());
      throw new IOException("S3에 파일 업로드 중 문제가 발생했습니다.");
    }

    return getImageUrl(fileName);
  }

  private String getImageUrl(String fileName) {
    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
  }


}
