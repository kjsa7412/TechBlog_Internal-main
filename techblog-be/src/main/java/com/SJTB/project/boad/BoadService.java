package com.SJTB.project.boad;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.data.ResultVo;
import com.SJTB.framework.security.SecurityUserUtil;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.framework.utils.FrameStringUtil;
import com.SJTB.project.base.BaseService;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogRepository;
import com.SJTB.project.gpt.GptResponseDto;
import com.SJTB.project.gpt.GptService;
import com.SJTB.project.img.ImgFileEntity;
import com.SJTB.project.img.ImgFileRepository;
import com.SJTB.project.md.MdService;
import com.SJTB.project.user.UserEntity;
import com.SJTB.project.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
//@RequiredArgsConstructor
public class BoadService extends BaseService {
    private final BoadRepository boadRepository;
    private final UserRepository userRepository;
    private final BoadContRepository boadContRepository;
    private final CmmtRepository cmmtRepository;
    private final BoadHashTagRepository boadHashTagRepository;
    private final ImgFileRepository imgFileRepository;

    private final MdService mdService;
    private final GptService gptService;

    @Value("${spring.config.allowedOrigin}")
    private String allowedOrigin;

    @Value("${thumbnail.path.url}")
    private String imgRequestUrl;

    public BoadService(ClntLogRepository clntLogRepository, SvrLogRepository svrLogRepository,
                       BoadRepository boadRepository, UserRepository userRepository, BoadContRepository boadContRepository,
                       CmmtRepository cmmtRepository, BoadHashTagRepository boadHashTagRepository, MdService mdService, ImgFileRepository imgFileRepository,
                       GptService gptService) {
        super(clntLogRepository, svrLogRepository);
        this.boadRepository = boadRepository;
        this.userRepository = userRepository;
        this.boadContRepository = boadContRepository;
        this.cmmtRepository = cmmtRepository;
        this.boadHashTagRepository = boadHashTagRepository;
        this.imgFileRepository = imgFileRepository;
        this.mdService = mdService;
        this.gptService = gptService;
    }

    // Select 에는 Transactional 안적어도됨
    @Transactional(readOnly = true)
    public ResultVo<List<BoadResponseDto>> BoadList() {
        ResultVo<List<BoadResponseDto>> resultVO = new ResultVo<>();
        try {
            List<BoadEntity> fetchJoinJPA = boadRepository.findAllByFetchJoin();

            List<BoadEntity> QueryDSLLeftJoin = boadRepository.findAllBoadQueryDSLByJoin();

            // 프론트로 던져줄 데이터 DTO로 가공
            List<BoadResponseDto> boadList = fetchJoinJPA.stream()
                    .map(data -> BoadResponseDto.builder()
                            .boadId(data.getBoadId())
                            .userId(data.getUser().getUserId())
                            .title(data.getTitle())
                            .summary(data.getSummary())
                            .useYN(data.getUseYN())
                            .openStatus(data.getOpenStatus())
                            .thumbnailpath(FrameStringUtil.setThumbnailUrlPath(data.getThumbnail()))
                            .views(data.getViews())
                            .likes(data.getLikes())
                            .lastMd(data.getLastMd())
                            .userId(data.getUser().getUserId())
                            .userName(data.getUser().getUserName())
                            .build())
                    .collect(Collectors.toList());

            resultVO.setContent(boadList);
        } catch( Exception e){
            e.printStackTrace();
        } finally {
//            게시글 목록 불러오는거 까지 로그 저장이 필요한가?
//            saveClientLog("",FrameConstants.BOARD_SEARCH,"",FrameHttpUtil);
        }
        return resultVO;
    }

//  게시글 검색 목록
    @Transactional
    public ResultVo<List<BoadResponseDto>> BoadList(HttpServletRequest request, BoadSearchRequestDto req){
        ResultVo<List<BoadResponseDto>> result = new ResultVo<>();
        try {
            List<BoadEntity> selectList = boadRepository.findAllByFetchJoin(req.getSearchWord());

            List<BoadResponseDto> boadList = selectList.stream()
                    .map(data -> BoadResponseDto.builder()
                            .boadId(data.getBoadId())
                            .userId(data.getUser().getUserId())
                            .title(data.getTitle())
                            .summary(data.getSummary())
                            .useYN(data.getUseYN())
                            .openStatus(data.getOpenStatus())
                            .thumbnailpath(FrameStringUtil.setThumbnailUrlPath(data.getThumbnail()))
                            .views(data.getViews())
                            .likes(data.getLikes())
                            .lastMd(data.getLastMd())
                            .userId(data.getUser().getUserId())
                            .userName(data.getUser().getUserName())
                            .build())
                    .collect(Collectors.toList());
            result.setContent(boadList);
        } catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At BoadList");
        } finally {
            //            게시글 목록 불러오는거 까지 로그 저장이 필요한가?
            saveClientLog("", FrameConstants.BOARD_SEARCH, 0,FrameHttpUtil.clientBroswserInfo(request));
        }


        return result;
    }

    /*인기 게시글 목록*/
    @Transactional
    public ResultVo<List<BoadResponseDto>> BoadPopularList() {
        ResultVo<List<BoadResponseDto>> result = new ResultVo<>();
        try{
//            Pageable pageable = PageRequest.of(0, 3, Sort.by("views").descending().by("likes").descending());
            List<BoadEntity> selectList = boadRepository.findPopularList();

//            List<BoadResponseDto> boadList = selectList.stream()
//                    .map(data -> BoadResponseDto.builder()
//                            .boadId(data.getBoadId())
//                            .userId(data.getUser().getUserId())
//                            .title(data.getTitle())
//                            .summary(data.getSummary())
//                            .useYN(data.getUseYN())
//                            .openStatus(data.getOpenStatus())
//                            .thumbnailpath(FrameStringUtil.setThumbnailUrlPath(data.getThumbnail()))
//                            .views(data.getViews())
//                            .likes(data.getLikes())
//                            .lastMd(data.getLastMd())
//                            .userId(data.getUser().getUserId())
//                            .userName(data.getUser().getUserName())
//                            .build())
//                    .collect(Collectors.toList());

            // boadId를 사용해서 프론트에서 md읽어 렌더링
            List<BoadResponseDto> boadList = selectList.stream()
                    .map(data -> BoadResponseDto.builder()
                            .boadId(data.getBoadId())
                            .build())
                    .collect(Collectors.toList());

            result.setContent(boadList);
        } catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At BoadPopularList");
        }
        return result;
    }

    /*게시물 자세히보기*/
    @Transactional
    public ResultVo BoadViewInfo(HttpServletRequest request, Integer req) {
        ResultVo result = new ResultVo();
        try{
            BoadEntity resultItem = boadRepository.findByBoadId(req);

            List<BoadContResponseDto> conList = resultItem.getConts().stream()
                    .map(data -> BoadContResponseDto.builder()
                                .contid(data.getContid())
                                .contcate(data.getContcate())
                                .cont(data.getCont())
                                .build()
                    ).collect(Collectors.toList());

            List<BoadHashTagEntity> resultList = boadHashTagRepository.fineAllHashTagByBoadid(req);
            String[] hashTagList = resultList.stream()
                    .map(BoadHashTagEntity::getHashtag)
                    .toArray(String[]::new);

            BoadResponseDto responseDto = BoadResponseDto.builder()
                                            .boadId(resultItem.getBoadId())
                                            .userId(resultItem.getUser().getUserId())
                                            .userName(resultItem.getUser().getUserName())
                                            .title(resultItem.getTitle())
                                            .openStatus(resultItem.getOpenStatus())
                                            .hashtag(hashTagList)
                                            .summary(resultItem.getSummary())
                                            .conts(conList)
                                            .lastMd(resultItem.getLastMd())
                                            .thumbnailid(resultItem.getThumbnail().getImgid())
                                            .thumbnailpath(FrameStringUtil.setThumbnailUrlPath(resultItem.getThumbnail()))
                                            .views(resultItem.getViews())
                                            .likes(resultItem.getLikes())
                                            .build();
            result.setContent(responseDto);
        } catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At BoadViewInfo");
        } finally {

        }
        return result;
    }

    /*
     * 게시글 입력
     * */
    @Transactional
    public ResultVo<BoadResponseDto> BoadInsert(HttpServletRequest request, BoadRequestDto reqBoad){
        ResultVo<BoadResponseDto> result = new ResultVo<>(BoadResponseDto.class);

        String userId = SecurityUserUtil.getCurrentUser();
        String userIP = FrameHttpUtil.getUserIp(request);
        String thumbnailPath = "";
        String gptAnswerContent = "";
        String summary = "";
        LocalDateTime nowTime = FrameDateUtil.getLocalDateTime();
        BoadEntity boadResult = new BoadEntity();
        ImgFileEntity imgFileEntity = null;
        try {
            String firstImgfileName = extractFirstImageFileName(reqBoad.getBoadConts());

            if(!firstImgfileName.equals("")) {
                imgFileEntity = imgFileRepository.findByImgName(firstImgfileName);
            }

            if(imgFileEntity != null && !FrameStringUtil.isNull(imgFileEntity.getImgid())) {
                imgFileEntity = imgFileRepository.getById(imgFileEntity.getImgid());
            } else {
                imgFileEntity = imgFileRepository.getById(1);
            }

            thumbnailPath = imgRequestUrl + FrameStringUtil.setThumbnailUrlPath(imgFileEntity);

            /*
             * 게시물 요약, 해시태그 생성
             * */
            ResultVo<GptResponseDto> summaryGPT = gptService.processGPTAPI(FrameConstants.CHATAPI_SUMMARY,userId, reqBoad.getBoadConts(),userIP);
            gptAnswerContent = summaryGPT.getContent().getAnswer();
            String hashTagList = "";
            if (gptAnswerContent != null && !gptAnswerContent.isEmpty() && gptAnswerContent.contains("||")) {
                String[] answerList = gptAnswerContent.split("\\|\\|");
                summary = answerList[0];
                hashTagList = answerList[1].replaceAll("#","").replaceAll(",","");
            } else {
                summary = makeInitSummarty(reqBoad);
                hashTagList = "기술블로그 게시물 개발자";
            }

            UserEntity user = userRepository.findById(userId);

            /// view, like 값 null 말고 0으로 넣어주기
            boadResult = boadRepository.save(BoadEntity.builder()
                .user(user)
                .title(reqBoad.getTitle())
                .useYN("Y")
                .openStatus("1")
                .thumbnail(imgFileEntity)
                .summary(summary)
                .views(0)
                .likes(0)
                .lastMd(1)
                .firsRegDt(nowTime)
                .firsRegId(userId)
                .firsRegIp(userIP)
                .finaRegDt(nowTime)
                .finaRegId(userId)
                .finaRegIp(userIP)
                .build());

            /*해시태그데이터에 boadId를 넣으려면 scope안에서 선언된 변수가필요*/
            BoadEntity finalBoadResult = boadResult;
            List<BoadHashTagEntity> boadHashTagEntities =
                    Arrays.stream(hashTagList.split(" "))
                            .map(item -> {
                                BoadHashTagEntity itemEntity = BoadHashTagEntity.builder()
                                        .boad(finalBoadResult)
                                        .hashtag(item)
                                        .firsRegDt(nowTime)
                                        .firsRegId(userId)
                                        .firsRegIp(userIP)
                                        .finaRegDt(nowTime)
                                        .finaRegId(userId)
                                        .finaRegIp(userIP)
                                        .build();
                                return itemEntity;
                            })
                            .collect(Collectors.toList());

            boadHashTagRepository.saveAll(boadHashTagEntities);

            // 결과값 가공
            result.setContent(BoadResponseDto.builder().boadId(boadResult.getBoadId()).build());

            /*
             * API 서버에 MD 파일 저장 프로세스 적용 필요
             * MD서비스 처리부분에서 GPT요약불러오기, 해시태그저장, 게시물 요약문 업데이트 주석처리
             * */
            mdService.makeMDFile(boadResult, reqBoad.getBoadConts());

            // NODE 서버에 MD 파일 저장 시작
            log.info("***** NODE 서버에 MD 저장 시작 *****");
            String url = allowedOrigin + "/api/createPost";

            HashMap<String, Object> param = new HashMap<String, Object>();

            param.put("slug", String.valueOf(boadResult.getBoadId()));
            param.put("title", reqBoad.getTitle());
            param.put("description", summary);
            param.put("thumbnail", thumbnailPath);
            param.put("keywords", cleanHashTagList(hashTagList));

            param.put("author", userId);
            param.put("datePublished", FrameDateUtil.formatLocalDateTimeWithPattern(boadResult.getFirsRegDt(), "yyyy-MM-dd HH:mm:ss"));
            param.put("dateModified", FrameDateUtil.formatLocalDateTimeWithPattern(boadResult.getFirsRegDt(), "yyyy-MM-dd HH:mm:ss"));
            param.put("content", reqBoad.getBoadConts());

            log.info("***** 파라미터 : " + param.toString() + " *****");

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);

            log.info("***** http 요청 시작 *****");
            // POST 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // 결과 message 값 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String message = jsonNode.get("message").asText();

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("***** http 요청 성공 *****");
            } else {
                result.setIsError(true);
                log.info("***** http 요청 실패 : " + message + " *****");
            }
        } catch( Exception e) {
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Boad Insert");
        } finally {
            if (boadResult.getBoadId() != null) {
                saveClientLog(userId, FrameConstants.BOARD_INSERT, boadResult.getBoadId(), FrameHttpUtil.clientBroswserInfo(request));
            }

            // 오류가 발생하였을 경우 수동 롤백
            if (result.getIsError()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return result;
    }

    /*게시글 작성시 기본 summary 내용 입력*/
    public String makeInitSummarty(BoadRequestDto boad){
        String summary = "";
        summary = "이 게시물은 "+boad.getTitle()+" 에 대한 내용입니다. 기술 블로그에서 작성되었습니다.";
        return summary;
    }

    /*게시물 수정*/
    //        1.boad_mst_info 정보변경
    //        2.boad_hash_info id에맞춰서 삭제 후 다시 insert
    //        3.boad_cont_info id에맞춰서 삭제 후 다시 insert
    //        4.sy_file_img_info 해당 게시글 이미지 삭제후 재업로드(기존이미지 굳이 다시 업로드 해야되는지?)
    //        5.MD파일 생성 프로세스 실행
    @Transactional
    public ResultVo<BoadResponseDto> BoadUpdate(HttpServletRequest request, BoadRequestDto reqBoad) {
        ResultVo<BoadResponseDto> result = new ResultVo<>(BoadResponseDto.class);

        String userId = SecurityUserUtil.getCurrentUser();
        String userIP = FrameHttpUtil.getUserIp(request);
        String thumbnailPath = "";
        String summaryContent = "";
        String summary = "";
        LocalDateTime nowTime = FrameDateUtil.getLocalDateTime();
        BoadEntity boadResult = new BoadEntity();
        ImgFileEntity imgFileEntity = null;
        try {
            // 업데이트 하려는 게시물 조회
            BoadEntity boadEntity = boadRepository.findById(reqBoad.getBoadid()).orElseThrow(()-> new Exception("게시물이 존재하지 않습니다."));

            // 쿠키의 ID와 게시물 작성한 ID가 동일한지 체크
            if (!boadEntity.getUser().getUserId().equals(userId)) {
                throw new Exception("본인이 작성한 게시물이 아닙니다.");
            }

            // 게시물의 첫번 째 이미지 추출
            String firstImgfileName = extractFirstImageFileName(reqBoad.getBoadConts());

            if(!firstImgfileName.equals("")) {
                imgFileEntity = imgFileRepository.findByImgName(firstImgfileName);
            }

            if(imgFileEntity != null && !FrameStringUtil.isNull(imgFileEntity.getImgid())) {
                imgFileEntity = imgFileRepository.getById(imgFileEntity.getImgid());
            } else {
                imgFileEntity = imgFileRepository.getById(1);
            }

            // 섬네일 이미지 가공
            thumbnailPath = imgRequestUrl + FrameStringUtil.setThumbnailUrlPath(imgFileEntity);

            /*
             * 게시물 요약, 해시태그 생성
             * */
            ResultVo<GptResponseDto> summaryGPT = gptService.processGPTAPI(FrameConstants.CHATAPI_SUMMARY,userId, reqBoad.getBoadConts(), userIP);
            summaryContent = summaryGPT.getContent().getAnswer();
            String hashTagList = "";
            if (summaryContent != null && !summaryContent.isEmpty() && summaryContent.contains("||")) {
                String[] answerList = summaryContent.split("\\|\\|");
                summary = answerList[0];
                    hashTagList = answerList[1].replaceAll("#","").replaceAll(",","");
            } else {
                summary = makeInitSummarty(reqBoad);
                hashTagList = "기술블로그 게시물 개발자";
            }

            // 해시태그 전체 삭제
            boadHashTagRepository.deleteHashTag(reqBoad.getBoadid());

            // 해시태그 전체 저장
            List<BoadHashTagEntity> boadHashTagEntities =
                    Arrays.stream(hashTagList.split(" "))
                            .map(item -> {
                                BoadHashTagEntity itemEntity = BoadHashTagEntity.builder()
                                        .boad(boadEntity)
                                        .hashtag(item)
                                        .firsRegDt(nowTime)
                                        .firsRegId(userId)
                                        .firsRegIp(userIP)
                                        .finaRegDt(nowTime)
                                        .finaRegId(userId)
                                        .finaRegIp(userIP)
                                        .build();
                                return itemEntity;
                            })
                            .collect(Collectors.toList());

            boadHashTagRepository.saveAll(boadHashTagEntities);

            // 필요한 내용 update
            UserEntity user = userRepository.findById(userId);
            boadRepository.updateBoadCont(
                    reqBoad.getTitle(),
                    summary,
                    imgFileEntity,
                    boadEntity.getLastMd() + 1,
                    userId,
                    userIP,
                    user,
                    reqBoad.getBoadid());

            // update된 자료 조회
            boadResult = boadRepository.findByBoadId(reqBoad.getBoadid());

            // 리턴값 가공
            result.setContent(BoadResponseDto.builder().boadId(boadResult.getBoadId()).build());

            /*
             * API 서버에 MD 파일 저장 프로세스 적용 필요
             * MD서비스 처리부분에서 GPT요약불러오기, 해시태그저장, 게시물 요약문 업데이트 주석처리
             * */
            mdService.makeMDFile(boadResult, reqBoad.getBoadConts());

            // NODE 서버에 MD 파일 저장 시작
            log.info("***** NODE 서버에 MD 저장 시작 *****");
            String url = allowedOrigin + "/api/createPost";

            HashMap<String, Object> param = new HashMap<String, Object>();

            param.put("slug", String.valueOf(boadResult.getBoadId()));
            param.put("title", reqBoad.getTitle());
            param.put("description", summary);
            param.put("thumbnail", thumbnailPath);
            param.put("keywords", cleanHashTagList(hashTagList));
            param.put("author", userId);
            param.put("datePublished", FrameDateUtil.formatLocalDateTimeWithPattern(boadResult.getFirsRegDt(), "yyyy-MM-dd HH:mm:ss"));
            param.put("dateModified", FrameDateUtil.formatLocalDateTimeWithPattern(boadResult.getFinaRegDt(), "yyyy-MM-dd HH:mm:ss"));
            param.put("content", reqBoad.getBoadConts());

            log.info("***** 파라미터 : " + param.toString() + " *****");

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);

            log.info("***** http 요청 시작 *****");
            // POST 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // 결과 message 값 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String message = jsonNode.get("message").asText();

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("***** http 요청 성공 *****");
            } else {
                result.setIsError(true);
                log.info("***** http 요청 실패 : " + message + " *****");
            }
        } catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Boad Update");
        } finally {
            if (boadResult.getBoadId() != null) {
                saveClientLog(userId, FrameConstants.BOARD_UPDATE, boadResult.getBoadId(), FrameHttpUtil.clientBroswserInfo(request));
            }

            // 오류가 발생하였을 경우 수동 롤백
            if (result.getIsError()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return result;
    }

    // 게시물 삭제(사용여부 변경으로 UPDATE)
    @Transactional
    public ResultVo<BoadResponseDto> BoadDelete(HttpServletRequest request, BoadRequestDto reqBoad) {
        ResultVo<BoadResponseDto> result = new ResultVo<>(BoadResponseDto.class);

        String userId = SecurityUserUtil.getCurrentUser();
        String userIP = FrameHttpUtil.getUserIp(request);

        try {
            // 삭제 하려는 게시물 조회
            BoadEntity boadEntity = boadRepository.findById(reqBoad.getBoadid()).orElseThrow(()-> new Exception("게시물이 존재하지 않습니다."));

            // 쿠키의 ID와 게시물 작성한 ID가 동일한지 체크
            if (!boadEntity.getUser().getUserId().equals(userId)) {
                throw new Exception("본인이 작성한 게시물이 아닙니다.");
            }

            // 게시물 상태 변경
            UserEntity user = userRepository.findById(userId);
            boadRepository.updateBoadStatus(
                    userId,
                    userIP,
                    user,
                    boadEntity.getBoadId());

            // 리턴값 가공
            result.setContent(BoadResponseDto.builder().boadId(boadEntity.getBoadId()).build());

            // MD 삭제 시작
            log.info("***** NODE 서버에 MD 삭제 시작 *****");
            String url = allowedOrigin + "/api/deletePost";

            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("slug", String.valueOf(boadEntity.getBoadId()));

            log.info("***** 파라미터 : " + param.toString() + " *****");

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);

            log.info("***** http 요청 시작 *****");
            // POST 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            // 결과 message 값 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String message = jsonNode.get("message").asText();

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("***** http 요청 성공 *****");
            } else {
                result.setIsError(true);
                log.info("***** http 요청 실패 : " + message + " *****");
            }
        } catch(Exception e) {
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Boad Delete");
        } finally {
            if (reqBoad.getBoadid() != null) {
                saveClientLog(userId, FrameConstants.BOARD_DELETE, reqBoad.getBoadid(), FrameHttpUtil.clientBroswserInfo(request));
            }

            // 오류가 발생하였을 경우 수동 롤백
            if (result.getIsError()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return result;
    }

    /*게시글 조회 및 좋아요 수*/
    @Transactional
    public ResultVo<BoadCountResponseDto> BoadCountList(Integer boadId){
        ResultVo<BoadCountResponseDto> result = new ResultVo<>(BoadCountResponseDto.class);

        try {
            // 조회수 + 1
            boadRepository.updateBoadView(boadId);

            BoadEntity boadEntity = boadRepository.findByBoadId(boadId);

            BoadCountResponseDto resultVO = BoadCountResponseDto.builder()
                                            .viewCnt(boadEntity.getViews())
                                            .likeCnt(boadEntity.getLikes())
                                            .build();

            result.setContent(resultVO);
        } catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Boad Like & Comment Count");
        }

        return result;
    }

    /*게시물 좋아요수 update*/
    @Transactional
    public ResultVo<BoadCountResponseDto> BoadUpdateLike(HttpServletRequest request, BoadRequestDto boadRequestDto) {
        ResultVo<BoadCountResponseDto> result = new ResultVo<>(BoadCountResponseDto.class);
        String userId = SecurityUserUtil.getCurrentUser();

        long updateRowCnt = 0;
        try {
            updateRowCnt = boadRepository.updateBoadLike(boadRequestDto);

            if (updateRowCnt == 0) {
                result.setIsError(true);
                result.setErrorMsg("Error At Boad Like");
            } else {
                BoadEntity boadEntity = boadRepository.findByBoadId(boadRequestDto.getBoadid());

                result.setContent(BoadCountResponseDto.builder().likeCnt(boadEntity.getLikes()).build());
            }
        } catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Boad Like");
        } finally {
            saveClientLog(userId, FrameConstants.VIEW_LIKE, boadRequestDto.getBoadid(), FrameHttpUtil.clientBroswserInfo(request));
        }

        return result;
    }

    /*게시글 조회시 댓글 목록 조회*/
    public ResultVo<List<CmmtResponseDto>> CommentList(Integer boadId){
        ResultVo<List<CmmtResponseDto>> result = new ResultVo<>();
        try {
            List<CmmtEntity> cmtList = cmmtRepository.findCmmtListByBoadId(boadId);

            List<CmmtResponseDto> cmts = cmtList.stream().map(
                    item -> {
                        CmmtResponseDto cmt = new CmmtResponseDto();
                        cmt.setCmtId(item.getCmtid());
                        cmt.setBoadId(item.getBoadid());
                        cmt.setUserId(item.getUser().getUserId());
                        cmt.setUserName(item.getUser().getUserName());
                        if (item.getUser().getProfilePic() != null && item.getUser().getProfilePic().getImgid() != null) {
                            cmt.setProfilePicPath(FrameStringUtil.setThumbnailUrlPath(item.getUser().getProfilePic()));
                        }
                        cmt.setCmt(item.getCmt());
                        cmt.setWriteDate(FrameDateUtil.formatLocalDateTimeWithPattern(item.getFirsRegDt(), "yyyy년 MM월 dd일"));
                        return cmt;
                    }).collect(Collectors.toList());

            result.setContent(cmts);
        }catch( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Loading Comment List");
        }
        return result;
    }

    /*게시물 댓글 입력*/
    @Transactional
    public ResultVo<List<CmmtResponseDto>> CommentInsert(HttpServletRequest request, CmmtRequestDto req) {
        ResultVo<List<CmmtResponseDto>> result = new ResultVo<>();
        String userId = SecurityUserUtil.getCurrentUser();

        try {
            CmmtEntity cmt = CmmtEntity.builder()
                    .boadid(req.getBoadId())
                    .cmt(req.getCmt())
                    .user(userRepository.findById(userId))
                    .useyn("Y")
                    .firsRegDt(LocalDateTime.now())
                    .firsRegId(userId)
                    .firsRegIp(FrameHttpUtil.getUserIp(request))
                    .build();

            // 댓글 insert
            CmmtEntity resultCmt = cmmtRepository.save(cmt);

            // 리턴할 댓글 목록 배열
            List<CmmtEntity> cmtList = new ArrayList<>();
            List<CmmtResponseDto> cmts = new ArrayList<>();

            // 댓글 작성 성공
            if (resultCmt.getCmtid() != null) {
                cmtList = cmmtRepository.findCmmtListByBoadId(req.getBoadId());

                cmts = cmtList.stream().map(
                        item -> {
                            CmmtResponseDto temp = new CmmtResponseDto();
                            temp.setCmtId(item.getCmtid());
                            temp.setBoadId(item.getBoadid());
                            temp.setUserId(item.getUser().getUserId());
                            temp.setUserName(item.getUser().getUserName());
                            if (item.getUser().getProfilePic() != null && item.getUser().getProfilePic().getImgid() != null) {
                                temp.setProfilePicPath(FrameStringUtil.setThumbnailUrlPath(item.getUser().getProfilePic()));
                            }
                            temp.setCmt(item.getCmt());
                            temp.setWriteDate(FrameDateUtil.formatLocalDateTimeWithPattern(item.getFirsRegDt(), "yyyy년 MM월 dd일"));
                            return temp;
                        }).collect(Collectors.toList());

                result.setContent(cmts);
            } else {
                result.setIsError(true);
                result.setErrorMsg("Error At Comment Insert");
            }
        } catch ( Exception e){
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Comment Insert");
        } finally {
            saveClientLog(userId, FrameConstants.BOARD_COMMENT_WRITE, req.getBoadId(), FrameHttpUtil.clientBroswserInfo(request));
        }
        return result;
    }

    /*게시물 댓글 삭제*/
    @Transactional
    public ResultVo<List<CmmtResponseDto>> CommentDelete(HttpServletRequest request, CmmtRequestDto req) {
        ResultVo<List<CmmtResponseDto>> result = new ResultVo<>();
        String userId = SecurityUserUtil.getCurrentUser();

        try {
            // 삭제하려는 댓글 조회
            CmmtEntity cmmtEntity = cmmtRepository.findById(req.getCmtId()).orElseThrow(()-> new Exception("댓글이 존재하지 않습니다."));

            // 쿠키의 ID와 댓글 작성한 ID가 동일한지 체크
            if (!cmmtEntity.getUser().getUserId().equals(userId)) {
                throw new Exception("본인이 작성한 댓글이 아닙니다.");
            }

            // 댓글 삭제
            cmmtRepository.deleteById(req.getCmtId());
            boolean exists = cmmtRepository.existsById(req.getCmtId());

            // 리턴할 댓글 목록 배열
            List<CmmtEntity> cmtList = new ArrayList<>();
            List<CmmtResponseDto> cmts = new ArrayList<>();

            // 댓글 삭제 성공
            if (!exists) {
                cmtList = cmmtRepository.findCmmtListByBoadId(req.getBoadId());

                cmts = cmtList.stream().map(
                        item -> {
                            CmmtResponseDto temp = new CmmtResponseDto();
                            temp.setCmtId(item.getCmtid());
                            temp.setBoadId(item.getBoadid());
                            temp.setUserId(item.getUser().getUserId());
                            temp.setUserName(item.getUser().getUserName());
                            if (item.getUser().getProfilePic() != null && item.getUser().getProfilePic().getImgid() != null) {
                                temp.setProfilePicPath(FrameStringUtil.setThumbnailUrlPath(item.getUser().getProfilePic()));
                            }
                            temp.setCmt(item.getCmt());
                            temp.setWriteDate(FrameDateUtil.formatLocalDateTimeWithPattern(item.getFirsRegDt(), "yyyy년 MM월 dd일"));
                            return temp;
                        }).collect(Collectors.toList());

                result.setContent(cmts);
            } else {
                result.setIsError(true);
                result.setErrorMsg("Error At Comment Delete");
            }
        } catch(Exception e) {
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Comment Delete");
        } finally {
            saveClientLog(userId, FrameConstants.BOARD_COMMENT_DELTE, req.getCmtId(), FrameHttpUtil.clientBroswserInfo(request));
        }

        return result;
    }


    //해시태그 필터링 및 배열 반환
    public String[] cleanHashTagList(String hashTagList){
        String[] items = hashTagList.split(" ");

        // 값이 없는 아이템을 제외하고 새로운 배열에 필터링
        List<String> resultList = new ArrayList<>();
        for (String item : items) {
            if (!FrameStringUtil.isEmpty(item.trim())) {
                resultList.add(item);
            }
        }

        // 리스트를 배열로 변환하여 반환
        return resultList.toArray(new String[0]);
    }

    //컨텐츠 내용안에서 첫번째 이미지해시파일명을 추출
    public String extractFirstImageFileName(String markdownContent) {
        String imagePattern = "!\\[.*?\\]\\((.*?)\\)"; // 이미지 URL을 찾는 정규식
        Pattern pattern = Pattern.compile(imagePattern);
        Matcher matcher = pattern.matcher(markdownContent);

        if (matcher.find()) {
            String imageUrl = matcher.group(1);
            String[] urlParts = imageUrl.split("/");
            return urlParts[urlParts.length - 1]; // 파일명 반환
        }

        return ""; // 이미지가 없으면 null 반환
    }
}

