package com.SJTB.project.img;

import com.SJTB.framework.data.ResultVo;
import com.SJTB.project.base.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImgController extends BaseController {

    private final ImgService imgService;

    // 이미지 조회 컨트롤러
    @GetMapping("/public/img")
    public ResponseEntity<Resource> showImage(@RequestParam String filePath) throws IOException {
        File file = new File(filePath);
        Resource resource = new UrlResource("file:" + file.getAbsolutePath());

        String contentType = Files.probeContentType(file.toPath()); // 확장자에 따른 MIME 타입 결정

        if (contentType == null) {
            contentType = "application/octet-stream"; // 기본 타입
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // 이미지 업로드 컨트롤러
    @PostMapping("/private/img/upload")
    public ResultVo<ImgResponseDto> uploadImageData(
            HttpServletRequest request,
            @RequestPart(name = "file") MultipartFile imageFile)
//            @RequestPart(value = "param") ImgRequestDto imgRequestDto)
    {
        return imgService.insertImage(request, imageFile);
    }

    private static final String UPLOAD_DIR = "C:/test/filePath/";

    // 파일 업로드 처리
    @PostMapping("/public/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        try {
            // 업로드할 파일의 경로 설정
            File outputFile = new File(UPLOAD_DIR + file.getOriginalFilename());
            // 디렉토리가 존재하지 않으면 생성
            outputFile.getParentFile().mkdirs();

            // 파일 저장
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(file.getBytes());
            }

            return ResponseEntity.ok("File uploaded successfully: " + outputFile.getPath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }
}
