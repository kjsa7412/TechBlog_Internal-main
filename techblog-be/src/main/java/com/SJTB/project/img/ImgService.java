package com.SJTB.project.img;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.data.ResultVo;
import com.SJTB.framework.security.SecurityUserUtil;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.framework.utils.FrameStringUtil;
import com.SJTB.project.base.BaseService;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class ImgService extends BaseService {

    @Value("${file.image.upload.path}")
    private String imgFileBasePath;

    @Value("${file.image.profile.upload.path}")
    private String profileImgFileBasePath;

    private final ImgFileRepository imgFileRepository;

    public ImgService(ClntLogRepository clntLogRepository, SvrLogRepository svrLogRepository, ImgFileRepository imgFileRepository){
        super(clntLogRepository, svrLogRepository);
        this.imgFileRepository = imgFileRepository;
    }

    @Transactional
    public ResultVo<ImgResponseDto> insertImage(HttpServletRequest request, MultipartFile file){
        ResultVo<ImgResponseDto> result = new ResultVo<>();
        String userId = SecurityUserUtil.getCurrentUser();

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null) {
            result.setIsError(true);
            result.setErrorMsg("Error At ImgUpload");
            result.setContent(new ImgResponseDto());
        }

        try {
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }

            // 파일명만 추출하여 해시화
            String filenameWithoutExtension = originalFilename.substring(0, dotIndex);
            String hashedFilename = hashFileName(filenameWithoutExtension);

            // 해시된 파일명 + 확장자로 최종 파일명 생성
            String finalFilename = hashedFilename + extension;

            // 저장할 경로 생성: 현재년도/현재월
            String datePath = new SimpleDateFormat("yyyy/MM").format(new Date());
            Path targetDirectory = Paths.get(imgFileBasePath, datePath);
            createDirectoryIfNotExists(targetDirectory);

            // 최종 파일 경로
            Path targetFile = targetDirectory.resolve(finalFilename);

            // 파일 저장
            file.transferTo(targetFile.toFile());

            ImgFileEntity imgFileEntity = ImgFileEntity.builder()
                                            .imgcate(FrameConstants.BOARD_IMAGE_UPLOAD)
//                                            .imgcateid(imgRequestDto.getBoadid())
                                            .imghashname(finalFilename)//해시이미지명
                                            .imgname(originalFilename)//이미지명
                                            .useyn("Y")
                                            .imgpath(targetDirectory.toString())
                                            .firsRegDt(FrameDateUtil.getLocalDateTime())
                                            .firsRegId(userId)
                                            .firsRegIp(FrameHttpUtil.getUserIp(request))
                                        .build();

            ImgFileEntity insResult = imgFileRepository.save(imgFileEntity);

            // 저장이 정상적으로 이루어지지 않으면 파일삭제
            if (insResult.getImgid() == null){
                Files.deleteIfExists(targetFile);
            } else {
                ImgResponseDto responseDto = new ImgResponseDto();
                responseDto.setImgname(originalFilename);
                responseDto.setImghashname(finalFilename);
                responseDto.setImgid(insResult.getImgid());
                responseDto.setImgpath(targetDirectory.toString());
                responseDto.setImgfullpath(FrameStringUtil.setThumbnailUrlPath(imgFileEntity));
                result.setContent(responseDto);
            }
        } catch( Exception e){
            result.setIsError(true);
            result.setErrorMsg(" Error At ImgUpload ");
            e.printStackTrace();
        } finally {
            saveClientLog(userId, FrameConstants.BOARD_IMAGE_UPLOAD, 0, FrameHttpUtil.clientBroswserInfo(request));
        }

        return result;
    }

    /**
    * 프로필 사진 저장용 메소드
    * 리턴받은 imgFileEntity 객체의 imgID가 존재하면 성공, 없으면 실패
    *
    * */
    @Transactional
    public ImgFileEntity insertProfileImage(HttpServletRequest request, MultipartFile file, String userId) {
        String originalFilename = file.getOriginalFilename();
        ImgFileEntity imgFileEntity = new ImgFileEntity();

        if (originalFilename == null) {
            return imgFileEntity;
        }

        try {
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }

            // 파일명만 추출하여 해시화
            String filenameWithoutExtension = originalFilename;
            int lastDotIndex = originalFilename.lastIndexOf('.');
            if (lastDotIndex != -1) {
                filenameWithoutExtension = originalFilename.substring(0, lastDotIndex);
            }
            String hashedFilename = hashFileName(filenameWithoutExtension);

            // 해시된 파일명 + 확장자로 최종 파일명 생성
            String finalFilename = hashedFilename + extension;

            // 저장할 경로 생성
            Path targetDirectory = Paths.get(profileImgFileBasePath);
            createDirectoryIfNotExists(targetDirectory);

            // 최종 파일 경로
            Path targetFile = targetDirectory.resolve(finalFilename);

            // 파일 저장
            file.transferTo(targetFile.toFile());

            imgFileEntity = ImgFileEntity.builder()
                    .imgcate(FrameConstants.BLOG_PF_IMAGE_INSERT)
                    .imghashname(finalFilename)//해시이미지명
                    .imgname(originalFilename)//이미지명
                    .useyn("Y")
                    .imgpath(targetDirectory.toString())
                    .firsRegDt(FrameDateUtil.getLocalDateTime())
                    .firsRegId(userId)
                    .firsRegIp(FrameHttpUtil.getUserIp(request))
                    .build();

            ImgFileEntity resultImgFileEntity = imgFileRepository.save(imgFileEntity);

            // 저장이 정상적으로 이루어지지 않으면 파일삭제
            if(resultImgFileEntity.getImgid() == null){
                Files.deleteIfExists(targetFile);
                return imgFileEntity;
            } else {
                return resultImgFileEntity;
            }
        } catch( Exception e){
            return imgFileEntity;
        } finally {
            saveClientLog(userId, FrameConstants.IMAGE_UPLOAD_PROFILE, 0, FrameHttpUtil.clientBroswserInfo(request));
        }
    }

    private String hashFileName(String filename) throws NoSuchAlgorithmException {
        String timestamp = FrameDateUtil.getDate("yyyyMMddHHmmss");
        String combined = filename + "_" + timestamp;

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(combined.getBytes());
        return bytesToHex(encodedhash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void createDirectoryIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

}
