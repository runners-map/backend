package com.service.runnersmap.service;

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

  @Value("${file.upload-dir}")
  private String uploadDir;

  private final FilesStorageRepository filesStorageRepository;

  /**
   * 파일 저장 로직
   */
  public FileStorage saveFile(MultipartFile file, User user, Post post) throws IOException {

    if (file.isEmpty()) {
      throw new IOException("파일이 존재하지 않습니다.");
    }

    String originalFileName = file.getOriginalFilename();
    String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

    // 이건 추후 삭제
    Path path = Paths.get(uploadDir).resolve(storedFileName);

//    ObjectMetadata metadata = new ObkectMetadata();
//    metadata.setContentLength(file.getSize());
//    try (InputStream inputStream = file.getInputStream()) {
//    amazonS3.putObject(new PutObjectRequest(bucketName, storedFileName, inputStream, metadata));
//    --------------------------------------------------------
//    try (InputStream inputStream = file.getInputStream()) {
//      // S3에 파일 업로드
//      s3Client.putObject(PutObjectRequest.builder())
//          .bucket(bucketName)
//          .key(storedFileName)
//          .build(),
//      RequestBody.fromInputStream(inputStream, file.getSize());
//    }

    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
      log.info("파일 저장 완료: {}", storedFileName);
    } catch (IOException e) {
      log.error("파일 저장 중 오류 발생: {}", e.getMessage());
      throw e;
    }

    // String fileUrl = String.format("~~~", bucketName, s3Client.region().id(), storedFileName);
    // String fileUrl = amazonS3.getUrl(bucketName, storedFileName).toString();

    FileStorage fileStorage = FileStorage.builder()
        .originalFileName(originalFileName)
        .storedFileName(storedFileName)
        .fileUrl(path.toString())
        // fileUrl(fileUrl)
        .fileSize(file.getSize())
        .user(user)
        .post(post)
        .build();
    return filesStorageRepository.save(fileStorage);
  }


  /**
   * 프로필 사진 업로드
   */
  @Transactional
  public FileStorage uploadProfileImage(MultipartFile file, User user) throws IOException {
    if (file.isEmpty()) {
      throw new IOException("파일이 존재하지 않습니다.");
    }

    // 기존 프로필 사진이 있으면 삭제
    FileStorage existingProfile = filesStorageRepository.findByUser(user);
    if (existingProfile != null) {
      // 기존 파일 삭제
      Files.deleteIfExists(Paths.get(uploadDir).resolve(existingProfile.getStoredFileName()));
//      // 기존 파일을 S3에서 삭제
//      s3Client.deleteObject(DeleteObjectRequest.builder()
//          .bucket(bucketName)
//          .key(existingProfile.getStoredFileName())
//          .build());
//    --------------------------------------------------
//      amazonS3.deleteObject(bucketName, existingProfile.getStoredFileName());
      // DB에서 레코드 삭제
      filesStorageRepository.delete(existingProfile);
      log.info("기존 프로필 사진 삭제 완료: {}", existingProfile.getStoredFileName());
      // 즉시 반영
      filesStorageRepository.flush();
    }

    // 새 파일 저장
    FileStorage uploadedFile = saveFile(file, user, null);
    uploadedFile.setFileUrl(
        Paths.get(uploadDir).resolve(uploadedFile.getStoredFileName()).toString());
    return uploadedFile;
  }


  /**
   * 인증샷 업로드
   */
  @Transactional
  public FileStorage uploadAfterRunPic(MultipartFile file, Post post) throws IOException {
    if (file.isEmpty()) {
      throw new IOException("파일이 존재하지 않습니다.");
    }

    FileStorage existingAfterRunPic = filesStorageRepository.findByPost(post);
    // 기존 사진 삭제
    if (existingAfterRunPic != null) {
      Files.deleteIfExists(Paths.get(uploadDir).resolve(existingAfterRunPic.getStoredFileName()));
      filesStorageRepository.delete(existingAfterRunPic);
      log.info("기존 인증샷 사진 삭제 완료 : {}", existingAfterRunPic.getStoredFileName());
      // 즉시 반영
      filesStorageRepository.flush();
    }
    // 새 파일 저장
    FileStorage uploadedFile = saveFile(file, null, post);
    uploadedFile.setFileUrl(
        Paths.get(uploadDir).resolve(uploadedFile.getStoredFileName()).toString());
    return uploadedFile;

    // S3에서 기존파일 삭제
    // 새 파일 S3에 저장
    // S3에 파일 업로드
    // 업로드된 파일정보 저장
    // DB에 저장
  }
}
