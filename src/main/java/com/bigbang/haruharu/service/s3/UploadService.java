package com.bigbang.haruharu.service.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.bigbang.haruharu.advice.error.DefaultException;
import com.bigbang.haruharu.advice.payload.ErrorCode;
import com.bigbang.haruharu.dto.response.ApiResponse;
import com.bigbang.haruharu.dto.response.ImageUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final AmazonS3Client amazonS3Client;

    private final String bucketName = "haruharu1";
    private final String folderName = "custom_image/";

    public ResponseEntity<?> imageUpload(MultipartFile multipartFile) {

        try {
            File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                    .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

            String fileName = "";

            fileName = folderName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름

            String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
            removeNewFile(uploadFile);
            return ResponseEntity.ok(ApiResponse.builder().check(true).information(new ImageUrlResponse(uploadImageUrl)).build());
        } catch (IOException e ) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        } catch (Exception e) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        }
    }

    private String putS3(File uploadFile, String fileName) {

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }


    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File("/Users/sangeun.seong/Desktop/wms/test" + "/" + UUID.randomUUID() + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }
    }

}
