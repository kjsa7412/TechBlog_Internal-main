package com.SJTB.project.md;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.framework.utils.FrameStringUtil;
import com.SJTB.project.base.BaseService;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogRepository;
import com.SJTB.project.boad.*;
import com.SJTB.project.gpt.GptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MdService extends BaseService {

    private final BoadHashTagRepository boadHashTagRepository;
    @Value("${file.markdown.upload.path}")
    private String mdfileBasePath;

    @Value("${file.markdown.nonversion.upload.path}")
    private String mdfileNoVersionPath;

    @Value("${file.uploadFile.url}")
    private String uploadFileUrl;

    private final MdFileRepository mdFileRepository;
    private final BoadRepository boadRepository;
    private final BoadContRepository boadContRepository;
    private final GptService gptService;

    public MdService(ClntLogRepository clntLogRepository, SvrLogRepository svrLogRepository, MdFileRepository mdFileRepository,
                     BoadRepository boadRepository, BoadContRepository boadContRepository, GptService gptService, BoadHashTagRepository boadHashTagRepository){
        super(clntLogRepository, svrLogRepository);
        this.mdFileRepository = mdFileRepository;
        this.boadRepository = boadRepository;
        this.boadContRepository = boadContRepository;
        this.gptService = gptService;
        this.boadHashTagRepository = boadHashTagRepository;
    }



    /*(게시글 최초등록) 게시글 정보가지고 MD파일 제작 후 DB MD파일 정보 저장
    * 게시글 정보 등록 return 정보 및 request정보 수신받음
    * 해당 정보를 토대로 MD파일을 양식에 맞춰서 파일생성
    * 생성한 파일 정보를 DB에 게시물정보에 version 및 md파일 정보 저장
    * */
    @Transactional
    public void
    makeMDFile(BoadEntity boad, String boadConts){


        String processSuccYN = "N";
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        // 경로 설정: basePath/현재년도/현재월
        String partPath = "/" + year + "/" + String.format("%02d", month);
        String filePath = mdfileBasePath + partPath;
        String fileName = boad.getBoadId().toString();
        String markdownFilePath = filePath;
        String version = "";
        try {
            long versionCnt = mdFileRepository.selectByCountByBoadId(boad.getBoadId());
            String nonVersionPath = mdfileNoVersionPath + "/" + fileName+".md";
            version = generateVersion((int) versionCnt);
            fileName += "_"+version+".md";
            markdownFilePath += "/" + fileName;

            // 해당 경로가 존재하지 않으면 생성
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            //기존 버전경로에 파일 저장
            writeMDFile(boad, boadConts, markdownFilePath);
            //최신MD파일 경로에도 복사 (사용안함)
//            writeMDFile(boad, boadConts, nonVersionPath);

            MdFileEntity mdFileEntity =  MdFileEntity.builder()
                                        .boadid(boad.getBoadId())
                                        .user(boad.getUser())
                                        .mdname(fileName)
                                        .mdpath(filePath)
                                        .mdversion(version)
                                        .firsRegDt(FrameDateUtil.getLocalDateTime())
                                        .firsRegId(boad.getUser().getUserId())
//                                        .firsRegIp(FrameHttpUtil.getUserIp(request))
                                        .build();

            MdFileEntity resultMd = mdFileRepository.save(mdFileEntity);

            long updateCnt = boadRepository.updateBoadMdInfo(resultMd.getMdid(),boad.getBoadId());
//            log.info("######### updateCnt : " + updateCnt);
//            gptService.summaryBoadCont(resultMd, boad, parseMarkdown(boadConts));

            /*프론트에서 MD파일 따로 제작해서 불필요*/
//            sendMarkdownFile(nonVersionPath);
            /*BoadService에서 실행하여 주석처리*/
//            sendMarkdownInfo(boad.getBoadId(), boadConts);

        } catch( Exception e){
            e.printStackTrace();
        } finally {
            processSuccYN = "Y";
            saveServerLog(boad.getUser().getUserId(), FrameConstants.MARKDOWN_FILE_MAKE,processSuccYN);
        }

    }

    // 기울임꼴과 굵은 글씨 처리 메서드
    private String processText(String text) {
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "**$1**"); // 기울임꼴 처리
        text = text.replaceAll("\\*(.*?)\\*", "*$1*"); // 굵은 글씨 처리
        return text;
    }

    //MD파일에 붙는 버전 생성
    private static String generateVersion(int count) {
        double majorVersion = 1; // 주요 버전
        double minorVersion = count * 0.1; // 부 버전
        double MAX_MINOR_VERSION = 9.0;
        // 부 버전이 최대값을 초과하면 주요 버전을 증가시키고 부 버전을 초기화
        if (minorVersion > MAX_MINOR_VERSION) {
            majorVersion += (int) (minorVersion / (MAX_MINOR_VERSION + 1)); // 필요한 만큼 주요 버전 증가
            minorVersion = minorVersion % (MAX_MINOR_VERSION + 1); // 부 버전 초기화
        }

        // 버전 문자열 생성
        return String.format("%.1f", majorVersion + minorVersion);
    }


    private void writeMDFile( BoadEntity boad, String boadCont, String filePath){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            //헤더 작성
            writer.write("---\n");//헤더시작
            // 1. 제목 작성
            writer.write("title: " + boad.getTitle() + "\n");

            // 2. summary 작성
            writer.write("description: " + boad.getSummary() + "\n");

            // 3. 썸네일 작성
            writer.write("thumbnail: " + FrameStringUtil.setThumbnailUrlPath(boad.getThumbnail()).replace(">-","") + "\n");

            // 4. 키워드 작성
            if (boad.getHashtag() != null && !boad.getHashtag().isEmpty()) {
                String hashTag = "";
                writer.write("Keywords: ");
                for (BoadHashTagEntity item : boad.getHashtag()) {
                    hashTag += item.getHashtag() + ",";
                }

                writer.write(String.join(", ", hashTag));
                writer.write("\n");
            }

            writer.write("openStatus: " + boad.getOpenStatus() + "\n");

            // 5. 작성자 작성
            writer.write("Author: " + boad.getUser().getUserId() + "\n");

            // 6. 작성일시 작성
            writer.write("datePublished: " + FrameDateUtil.formatLocalDateTimeWithPattern(boad.getFirsRegDt(), "yyyy-MM-dd HH:mm:ss") + "\n");

            // 7. 수정일시 작성
            writer.write("dateModified: " + FrameDateUtil.formatLocalDateTimeWithPattern(boad.getFinaRegDt(), "yyyy-MM-dd HH:mm:ss") + "\n");

            //헤더 작성 종료
            writer.write("---\n");

            writer.write(boadCont);

            // 8. 게시물 내용 작성
            writer.close();

            String content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch( Exception e){
            e.printStackTrace();
        }
    }

    public void sendMarkdownFile(String filePath){
        try {
            File file = new File(filePath);
            FileSystemResource resource = new FileSystemResource(file);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            // 멀티파트 맵 생성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource); // 키 "file"로 지정

            // HttpEntity 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    uploadFileUrl, // 서버 URL
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("File uploaded successfully: {}", response.getBody());
            } else {
                log.error("File upload failed with status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMarkdownInfo(int boadid, String boadCont){
        try {
            MdResponseDto mdDto = new MdResponseDto();
            BoadEntity resultItem = boadRepository.findByBoadId(boadid);
            List<BoadHashTagEntity> hashTagList = boadHashTagRepository.fineAllHashTagByBoadid(boadid);
            mdDto.setSlug(String.valueOf(boadid));
            mdDto.setTitle(resultItem.getTitle());
            mdDto.setDescription(resultItem.getSummary());
            mdDto.setThumbnail(FrameStringUtil.setThumbnailUrlPath(resultItem.getThumbnail()));
            if(hashTagList.size() > 0){
                String hashTag = hashTagList.stream()
                        .map(BoadHashTagEntity::getHashtag)
                        .filter(hashtag -> hashtag != null && !hashtag.isEmpty())
                        .collect(Collectors.joining(", "));
                mdDto.setKeywords(hashTag.replaceAll(",,", ","));
            }
            log.info("######### hashTag: " + mdDto.getKeywords());
            mdDto.setAuthor(resultItem.getUser().getUserId());
            mdDto.setDatePublished(FrameDateUtil.formatLocalDateTimeWithPattern(resultItem.getFirsRegDt(),""));
            if(resultItem.getFinaRegDt() == null){
                mdDto.setDateModified(FrameDateUtil.formatLocalDateTimeWithPattern(resultItem.getFirsRegDt(),""));
            } else {
                mdDto.setDateModified(FrameDateUtil.formatLocalDateTimeWithPattern(resultItem.getFinaRegDt(),""));
            }
            mdDto.setContent(boadCont);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8089/api/createPost";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MdResponseDto> requestEntity = new HttpEntity<>(mdDto, headers);

            // POST 요청 전송
            MdResponseDto response = restTemplate.postForObject(url, requestEntity, MdResponseDto.class);

            // 응답 출력
            if (response != null) {
                log.info("Post created: " + response);
            } else {
                log.info("Failed to create post.");
            }

        } catch (Exception e){
            log.error(FrameConstants.ERROR_CONTROLLER, e);
        }
    }

    public static String parseMarkdown(String markdown) {
        List<MarkdownElement> sections = new ArrayList<>();
        StringBuilder codeBlock = new StringBuilder();
        boolean inCodeBlock = false;

        // Markdown을 줄 단위로 분리
        String[] lines = markdown.split("\n");
        StringBuffer sb = new StringBuffer();
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                if (line.startsWith("```")) {
                    if (inCodeBlock) {
                        // 코드 블록 종료
                        sections.add(new MarkdownElement(codeBlock.toString(), "code", 0));
                        codeBlock.setLength(0); // StringBuilder 초기화
                        inCodeBlock = false;
                    } else {
                        // 코드 블록 시작
                        inCodeBlock = true;
                    }
                } else {
                    if (inCodeBlock) {
                        codeBlock.append(line).append("\n");
                    } else {
                        sb.append(classifyLine(line)).append("\n");
                    }
                }
            }
        }

        log.info("### " + sb.toString());

        return sb.toString();
    }

    public static String classifyLine(String line) {
        if (line.startsWith("# ")) {
            return line;
        } else if (line.startsWith("## ")) {
            return line;
        } else if (line.startsWith("### ")) {
            return line;
        } else if (line.startsWith("#### ")) {
            return line;
        } else if (line.startsWith("##### ")) {
            return line;
        } else if (line.startsWith("###### ")) {
            return line;
        } else if (line.startsWith("***")) {
            return line;
        } else if (line.trim().startsWith("* ") || line.trim().startsWith("- ")) {
            return line;
        } else if (line.trim().matches("^\\d+\\. .*")) {
            return line;
        } else {
            return line;
        }

    }

    // Depth 계산
    public static int countDepth(String line) {
        int depth = 0;
        while (line.startsWith("  ")) { // 두 개의 공백으로 깊이 증가
            depth++;
            line = line.substring(2);
        }
        return depth;
    }

}
